import React, {useEffect, useState} from "react";
import {getAuction} from '@/api/auction.js'
import {createBid} from '@/api/bid.js'
import {useParams} from "react-router-dom";
import {Button, Input} from "@material-tailwind/react";
import {createComment, getComments} from "@/api/comment.js";

export function AuctionDetails() {
  const auctionId = useParams();
  const [auctionResponseDto, setAuctionResponseDto] = useState([]);
  const [comments, setComments] = useState([])
  const [events, setEvents] = useState([]);

  useEffect(() => {
    console.log("event 실행")
    const eventSource = new EventSource(`http://localhost:8081/stream/auctions/${auctionId.auctionId}`);
    console.log(eventSource)

    eventSource.onmessage = (event) => {
      // 서버로부터 받은 데이터 처리
      const eventData = JSON.parse(event.data);
      console.log(eventData)
      document.querySelector('#bid-price').textContent = eventData.price;
      setEvents(prevEvents => [...prevEvents, eventData]);
    };

    eventSource.onerror = (error) => {
      console.error('SSE Error:', error);
      eventSource.close();
    };

    return () => {
      eventSource.close(); // 컴포넌트가 unmount될 때 EventSource 연결 해제
    };
  }, [auctionId]);


  const getAuctionDto = () => {
    getAuction(auctionId.auctionId)
    .then((response) => {
      setAuctionResponseDto(response.data);
    })
  }
  const setCommentsDto = () => {
    getComments(auctionId.auctionId)
    .then((response) => {
      console.log(response.data.data)
      setComments(response.data.data);
    })
  }

  useEffect(() => {
    getAuctionDto();
    if (auctionResponseDto.statusEnum != "ON_SALE") {
      setCommentsDto();
    }
  }, [])

  const submitBid = async (event) => {
    event.preventDefault();
    const bidPrice = event.target[0].value;
    try {
      const response = await createBid(auctionId.auctionId, bidPrice)

      if (response.data.status == "BAD_REQUEST") {
        alert(response.data.message)
      } else {
        document.querySelector('#bid-price').textContent = bidPrice;
        alert("입찰이 완료되었습니다.")
      }

    } catch (error) {
      console.log(error)
    }
  };

  const submitComment = async (event) => {
    event.preventDefault();
    const comment = event.target[0].value;
    try {
      const response = await createComment(auctionId.auctionId, comment)

      if (response.data.status == "BAD_REQUEST") {
        alert(response.data.message)
      } else {
        alert("댓글 작성이 완료되었습니다.")
      }

    } catch (error) {
      console.log(error)
    }
  };

  return (
      <div>
        <br/>
        <br/>
        <div
            className="relative flex flex-col bg-clip-border rounded-xl bg-white text-gray-700 shadow-md">
          <div
              className="relative bg-clip-border mx-4 rounded-xl overflow-hidden bg-gradient-to-tr from-gray-900 to-gray-800 text-white shadow-gray-900/20 shadow-lg -mt-6 mb-8 p-6">
            {auctionResponseDto.title}
          </div>
          <span
              className="align-right">{auctionResponseDto.sellerNickname}</span>
          <br/>

          <img
              src={auctionResponseDto.filePath}
              className="h-full w-full object-cover"
          />

          <hr className="my-8 border-blue-gray-50"/>
          <span className="font-bold">{auctionResponseDto.content}</span>
          <br/>

        </div>

        <br/>
        <br/>

        {auctionResponseDto.statusEnum === 'ON_SALE' ? (
            <div>
              <div
                  className="align-middle select-none font-sans bg-white font-bold text-center uppercase transition-all disabled:opacity-50 disabled:shadow-none disabled:pointer-events-none text-xs py-2 px-4 rounded-lg border border-gray-900 text-gray-900 hover:opacity-75 focus:ring focus:ring-gray-300 active:opacity-[0.85]">
                현재 입찰가 : <span
                  id={"bid-price"}>{auctionResponseDto.price}</span>
              </div>
              <br/>
              <div
                  className="align-middle select-none font-sans font-bold text-center uppercase transition-all disabled:opacity-50 disabled:shadow-none disabled:pointer-events-none text-xs py-2 px-4 rounded-lg border border-gray-900 text-gray-900 hover:opacity-75 focus:ring focus:ring-gray-300 active:opacity-[0.85]">
                <form onSubmit={submitBid} className="margin-50px">
                  <div className="mb-1 flex flex-col gap-6">
                    <Input
                        type="price"
                        size="lg"
                        placeholder="숫자만 입력하세요"
                        className=" !border-t-blue-gray-200 focus:!border-t-gray-900 bg-white"
                        labelProps={{
                          className: 'before:content-none after:content-none',
                        }}
                    />
                  </div>
                  <Button className="mt-6" fullWidth type={'submit'}>
                    입찰하기
                  </Button>
                </form>
              </div>
            </div>
        ) : auctionResponseDto.statusEnum === 'HOLD' ? (

            <div>
              <div
                  className="align-middle select-none font-sans bg-white font-bold text-center uppercase transition-all disabled:opacity-50 disabled:shadow-none disabled:pointer-events-none text-xs py-2 px-4 rounded-lg border border-gray-900 text-gray-900 hover:opacity-75 focus:ring focus:ring-gray-300 active:opacity-[0.85]">
                최고 입찰가 : <span
                  id={"bid-price"}>{auctionResponseDto.price}</span>
                <br/>
                최고 낙찰자 : <span>{auctionResponseDto.buyerNickname}</span>
              </div>
              <br/>
              <form onSubmit={submitComment} className="margin-50px">
                <div className="mb-1 flex flex-col gap-6">
                  <Input
                      type="text"
                      size="lg"
                      placeholder="판매자, 구매자만 댓글을 입력하실 수 있습니다."
                      className=" !border-t-blue-gray-200 focus:!border-t-gray-900 bg-white"
                      labelProps={{
                        className: 'before:content-none after:content-none',
                      }}
                  />
                </div>
                <Button className="mt-6" fullWidth type={'submit'}>
                  댓글달기
                </Button>
              </form>
              <div
                  className="relative flex flex-col bg-clip-border rounded-xl bg-white text-gray-700 shadow-md">
                <table className="w-full min-w-[640px] table-auto">
                  <thead>
                  <tr>
                    <th className="border-b border-blue-gray-50 py-3 px-6 text-left">
                      <p className="block antialiased font-sans text-[11px] font-medium uppercase text-blue-gray-400">작성자</p>
                    </th>
                    <th className="border-b border-blue-gray-50 py-3 px-6 text-left">
                      <p className="block antialiased font-sans text-[11px] font-medium uppercase text-blue-gray-400">내용</p>
                    </th>
                    <th className="border-b border-blue-gray-50 py-3 px-6 text-left">
                      <p className="block antialiased font-sans text-[11px] font-medium uppercase text-blue-gray-400">작성시간</p>
                    </th>
                  </tr>
                  </thead>
                  <tbody>
                  {comments.map((commentResponseDto, index) => (
                      <tr key={index}>
                        <td className="py-3 px-5 border-b border-blue-gray-50">
                          <p className="block antialiased font-sans text-xs font-medium text-blue-gray-600">{commentResponseDto.nickname}</p>
                        </td>
                        <td className="py-3 px-5 border-b border-blue-gray-50">
                          <p className="block antialiased font-sans text-xs font-medium text-blue-gray-600">{commentResponseDto.content}</p>
                        </td>
                        <td className="py-3 px-5 border-b border-blue-gray-50">
                          <p className="block antialiased font-sans text-xs font-medium text-blue-gray-600">{commentResponseDto.createdAt}</p>
                        </td>
                      </tr>
                  ))}
                  </tbody>
                </table>
              </div>

            </div>

        ) : (
            <div
                className="align-middle select-none font-sans bg-white font-bold text-center uppercase transition-all disabled:opacity-50 disabled:shadow-none disabled:pointer-events-none text-xs py-2 px-4 rounded-lg border border-gray-900 text-gray-900 hover:opacity-75 focus:ring focus:ring-gray-300 active:opacity-[0.85]">
              최고 입찰가 : <span
                id={"bid-price"}>{auctionResponseDto.price}</span>
              <br/>
              최고 낙찰자 : <span>{auctionResponseDto.buyerNickname}</span>
            </div>
        )}
      </div>
  );

}