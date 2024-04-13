import { Axios } from "@/configs/axios.js";

// Create a bid on an auction
export const createBid = async (auctionId, bidData) => {
    return await Axios.post(`/v1/auctions/${auctionId}/bids`, bidData);
};
