export const healthCheck = async () => {
    return await Axios.get("/health");
};