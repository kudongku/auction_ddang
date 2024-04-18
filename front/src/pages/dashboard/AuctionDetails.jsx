import React, {useEffect, useState} from "react";
import {getAuction} from '@/api/auction.js'
import {createBid} from '@/api/bid.js'
import {Link, useParams} from "react-router-dom";
import {Button, Input, Typography} from "@material-tailwind/react";

export function AuctionDetails() {
  const auctionId = useParams();
  const [auctionResponseDto, setAuctionResponseDto] = useState([]);

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
      await createBid(auctionId.auctionId, bidPrice);
    }catch (error){
      console.log(error)
    }
  }

  return (
      <div >
        <div className="border border-blue-gray-100 shadow-sm">
          <h1 className="font-extrabold text-4xl">
            {auctionResponseDto.title}
          </h1>
          <img
              src={auctionResponseDto.filePath}
              className="h-full w-full object-cover"
          />
          <span className="float-right">{auctionResponseDto.sellerNickname}</span><br/>
          <span className="font-bold">{auctionResponseDto.content}</span><br/>

        </div>

        <br/>
        <br/>

        <div className="border border-blue-gray-100 shadow-sm">
          현재 입찰가 : {auctionResponseDto.price}
        </div>



        <form
            onSubmit={submitBid}
            className="mx-auto mb-2 mt-8 w-80 max-w-screen-lg lg:w-1/2"
        >
          <div className="mb-1 flex flex-col gap-6">
            <Input
                type="price"
                size="lg"
                placeholder="숫자만 입력하세요"
                className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
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
  );

}