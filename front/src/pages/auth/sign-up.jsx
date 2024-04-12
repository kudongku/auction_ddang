import {Input, Button, Typography} from "@material-tailwind/react";
import { Link } from "react-router-dom";
import axios from "axios";
import {useState} from "react";

function SignUp() {
  const [formData, setFormData] = useState({
    email: "",
    nickname: "",
    password: "",
    passwordConfirm : "",
    address : "서울시 강남구 서초동"
  });

  const handleChange = (e) => {
    console.log(e.target.name)
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    alert("!");
    e.preventDefault();
    try {
      const response = await axios.post("http://localhost:5173/api/v1/users/signup", formData);
      console.log("Registered successfully!", response.data);
      // 여기서 성공적으로 등록되었음을 처리할 수 있습니다.
    } catch (error) {
      console.error("Registration failed:", error);
      // 여기서 등록 실패에 대한 처리를 할 수 있습니다.
    }
  };

  return (
      <section className="m-8 flex">
        <div className="w-2/5 h-full hidden lg:block">
          <img
              src="/img/pattern.png"
              className="h-full w-full object-cover rounded-3xl"
          />
        </div>
        <div className="w-full lg:w-3/5 flex flex-col items-center justify-center">
          <div className="text-center">
            <Typography variant="h2" className="font-bold mb-4">Sign Up</Typography>
            <Typography variant="paragraph" color="blue-gray" className="text-lg font-normal">이메일, 닉네임, 비밀번호를 입력해주세요</Typography>
          </div>
          <form className="mt-8 mb-2 mx-auto w-80 max-w-screen-lg lg:w-1/2" onSubmit={handleSubmit}>
            <div className="mb-1 flex flex-col gap-6">
              <Typography variant="small" color="blue-gray" className="-mb-3 font-medium">
                이메일
              </Typography>
              <Input
                  size="lg"
                  placeholder="name@mail.com"
                  className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
                  labelProps={{
                    className: "before:content-none after:content-none",
                  }}
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
              />
            </div>

            <div className="mb-1 flex flex-col gap-6">
              <Typography
                  variant="small"
                  color="blue-gray"
                  className="-mb-3 font-medium"
              >
                닉네임
              </Typography>
              <Input
                  size="lg"
                  placeholder="Nickname"
                  className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
                  labelProps={{
                    className: "before:content-none after:content-none",
                  }}
                  name="nickname"
                  value={formData.nickname}
                  onChange={handleChange}
              />
            </div>

            <div className="mb-1 flex flex-col gap-6">
              <Typography
                  variant="small"
                  color="blue-gray"
                  className="-mb-3 font-medium"
              >
                비밀번호
              </Typography>
              <Input
                  size="lg"
                  placeholder="Password"
                  type="password"
                  className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
                  labelProps={{
                    className: "before:content-none after:content-none",
                  }}
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
              />
            </div>

            <div className="mb-1 flex flex-col gap-6">
              <Typography
                  variant="small"
                  color="blue-gray"
                  className="-mb-3 font-medium"
              >
                비밀번호 재확인
              </Typography>
              <Input
                  size="lg"
                  placeholder="Confirm Password"
                  type="password"
                  className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
                  labelProps={{
                    className: "before:content-none after:content-none",
                  }}
                  name="passwordConfirm"
                  value={formData.passwordConfirm}
                  onChange={handleChange}
              />
            </div>

            <Button className="mt-6" fullWidth  type={"submit"}>
              등록하기
            </Button>
          </form>

        </div>
      </section>
  );
}

export default SignUp;