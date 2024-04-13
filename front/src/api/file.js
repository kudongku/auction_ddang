import { Axios } from "@/configs/axios.js";

// Upload an image for an auction
export const uploadImage = async (auctionImage, imageName) => {
    const formData = new FormData();
    formData.append("auctionImage", auctionImage);
    formData.append("requestDto", JSON.stringify({ imageName }));

    return await Axios.post("/v1/auctions/files", formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
};

// Get pre-signed URL for a file
export const getPreSignedUrl = async (fileId) => {
    return await Axios.get(`/v1/auctions/files/${fileId}`);
};
