import { Axios } from "@/configs/axios.js";

// Create an auction
export const createAuction = async (auctionData) => {
    return await Axios.post("/v1/auctions", auctionData);
};

// Delete an auction
export const deleteAuction = async (auctionId) => {
    return await Axios.delete(`/v1/auctions/${auctionId}`);
};

// Get a list of all auctions
export const getAuctions = async (status, title, pageable) => {
    const params = { status, title, ...pageable };
    return await Axios.get("/v1/auctions", { params });
};

// Get details of a specific auction
export const getAuction = async (auctionId) => {
    return await Axios.get(`/v1/auctions/${auctionId}`);
};

// Complete an auction
export const completeAuction = async (auctionId) => {
    return await Axios.patch(`/v1/auctions/${auctionId}`);
};

// Get auctions created by the logged-in user
export const getMyAuctions = async (pageable) => {
    return await Axios.get("/v1/auctions/myauctions", { params: pageable });
};

// Get auctions that the user has bid on
export const getMyBids = async (pageable) => {
    return await Axios.get("/v1/auctions/mybids", { params: pageable });
};
