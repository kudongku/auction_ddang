import { Axios } from "@/configs/axios.js";

// Create a bid on an auction
export const createBid = async (auctionId, price) => {
    return await Axios.post(`/v1/auctions/${auctionId}/bids`, {price});
};
