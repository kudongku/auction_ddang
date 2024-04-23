import { Button, Input, Typography } from '@material-tailwind/react';
import { useState } from 'react';
import { createAuction } from '@/api/auction.js'; // 경매 생성 API 함수
import { uploadImage } from "@/api/file.js";
import {useNavigate} from "react-router-dom";

function CreateAuction() {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [image, setImage] = useState(null);
  const [description, setDescription] = useState('');
  const [ObjectName, setObjectName] = useState("");

  const handleImageChange = (event) => {
    // 이미지 업로드 처리
    const selectedImage = event.target.files[0];
    setImage(selectedImage);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    // 경매 생성 요청 보내기
    try {
      const response = await uploadImage(image, ObjectName);
      console.log( response.data.data.fileId);

      const formData = new FormData();
      formData.append('title', title);
      formData.append('fileId', response.data.data.fileId);
      formData.append('content', description);

      await createAuction(formData);
      navigate(`/dashboard/home`);
      // 경매 생성 후 필요한 작업 수행
      // 예: 경매 목록 페이지로 이동
    } catch (error) {
      console.error('Failed to create auction:', error);
      alert('경매 생성에 실패했어요.\n' + error);
    }
  };

  return (
      <section className="m-8 flex gap-4">
        <div className="mt-24 w-full lg:w-3/5">
          <div className="text-center">
            <Typography variant="h2" className="mb-4 font-bold">
              Create Auction
            </Typography>
            <Typography
                variant="paragraph"
                color="blue-gray"
                className="text-lg font-normal"
            >
              경매 정보를 입력해주세요.
            </Typography>
          </div>
          <form
              onSubmit={handleSubmit}
              className="mx-auto mb-2 mt-8 w-80 max-w-screen-lg lg:w-1/2"
          >
            <div className="mb-6 flex flex-col gap-6">
              <Typography
                  variant="small"
                  color="blue-gray"
                  className="-mb-3 font-medium"
              >
                Auction Title
              </Typography>
              <Input
                  type="text"
                  size="lg"
                  placeholder="Enter auction title"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
              />
              <Typography
                  variant="small"
                  color="blue-gray"
                  className="-mb-3 font-medium"
              >
                Upload Image
              </Typography>
              <Input
                  type="file"
                  size="lg"
                  onChange={handleImageChange}
                  className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
              />
              <Typography
                  variant="small"
                  color="blue-gray"
                  className="-mb-3 font-medium"
              >
                Object Name
              </Typography>
              <Input
                  rows={5}
                  placeholder="Enter auction description"
                  value={ObjectName}
                  onChange={(e) => setObjectName(e.target.value)}
                  className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
              />
              <Typography
                  variant="small"
                  color="blue-gray"
                  className="-mb-3 font-medium"
              >
                Description
              </Typography>
              <Input
                  rows={5}
                  placeholder="Enter auction description"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
              />
            </div>

            <Button className="mt-6" fullWidth type="submit">
              Create Auction
            </Button>
          </form>
        </div>
      </section>
  );
}

export default CreateAuction;
