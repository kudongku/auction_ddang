import {Axios} from "@/configs/axios.js";

export const healthCheck = async () => {
    return await Axios.get("/health");
};