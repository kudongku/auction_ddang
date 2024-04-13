import { Axios } from "@/configs/axios.js";

// Create a comment on an auction
export const createComment = async (auctionId, commentData) => {
    return await Axios.post(`/v1/auctions/${auctionId}/comments`, commentData);
};

// Get comments on an auction
export const getComments = async (auctionId) => {
    return await Axios.get(`/v1/auctions/${auctionId}/comments`);
};
