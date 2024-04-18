import React, {useEffect, useState} from "react";
import {getAuction} from '@/api/auction.js'
import {createBid} from '@/api/bid.js'
import {useNavigate, useParams} from "react-router-dom";
import {Button, Input} from "@material-tailwind/react";

export function AuctionDetails() {
  const auctionId = useParams();
  const [auctionResponseDto, setAuctionResponseDto] = useState([]);
  const navigate = useNavigate();

  const getAuctionDto = () => {
    getAuction(auctionId.auctionId)
    .then((response) => {
      setAuctionResponseDto(response.data);
    })
  }

  useEffect(() => {
    getAuctionDto();
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
  }

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

        <div
            className="align-middle select-none font-sans bg-white font-bold text-center uppercase transition-all disabled:opacity-50 disabled:shadow-none disabled:pointer-events-none text-xs py-2 px-4 rounded-lg border border-gray-900 text-gray-900 hover:opacity-75 focus:ring focus:ring-gray-300 active:opacity-[0.85]">
          현재 입찰가 :
          <span id={"bid-price"}>{auctionResponseDto.price}</span>
        </div>

        <br/>
        <div
            className="align-middle select-none font-sans font-bold text-center uppercase transition-all disabled:opacity-50 disabled:shadow-none disabled:pointer-events-none text-xs py-2 px-4 rounded-lg border border-gray-900 text-gray-900 hover:opacity-75 focus:ring focus:ring-gray-300 active:opacity-[0.85]">
          <form
              onSubmit={submitBid}
              className="margin-50px"
          >
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
  );

}