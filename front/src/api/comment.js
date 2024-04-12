import {Axios} from "@/configs/axios.js";

export const createComment = async (auctionId, requestDto) => await Axios.post(`/api/v1/auctions/${auctionId}/comments`, requestDto);