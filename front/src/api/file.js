import { Axios } from "@/configs/axios.js";

// Upload an image for an auction
export const uploadImage = async (auctionImage, imageName) => {
    const json = JSON.stringify(imageName);
    const blob = new Blob([json], { type: "application/json" });
    const formData = new FormData();
    formData.append("auctionImage", auctionImage );
    formData.append("requestDto", blob);

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
