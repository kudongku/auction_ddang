import {Axios} from "@/configs/axios.js";

export const signup = async (formData) => await Axios.post("/api/v1/users/signup", formData);