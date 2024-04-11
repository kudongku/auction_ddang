import {Button, Input, Typography,} from "@material-tailwind/react";
import {Link} from "react-router-dom";
import axios from "axios";

function SignIn() {
  const handleSubmit = async (event) => {
    //폼 데이터 처리 시 다른 페이지로 이동, 새로고침 되는것을 막음
    //-> 이메일 입력 시 값을 받아올 떄 다른 페이지로 이동X
    event.preventDefault();

    const email = event.target[0].value;
    const password = event.target[1].value;

    console.log(1, email, password);
    await axios.post("http://localhost:5173/api/v1/users/signin",
        {email, password}).then(
        function (response) {
          //호출 성공 시 데이터 저장 코드
          console.log(response);
        }).catch(function (error) {
      //호출 실패 시 에러처리 코드
    });

  };
  return (
      <section className="m-8 flex gap-4">
        <div className="w-full lg:w-3/5 mt-24">
          <div className="text-center">
            <Typography variant="h2" className="font-bold mb-4">Sign
              In</Typography>
            <Typography variant="paragraph" color="blue-gray"
                        className="text-lg font-normal">이메일과 비밀번호를
              입력해주세요.</Typography>
          </div>
          <form onSubmit={handleSubmit}
                className="mt-8 mb-2 mx-auto w-80 max-w-screen-lg lg:w-1/2">
            <div className="mb-1 flex flex-col gap-6">
              <Typography variant="small" color="blue-gray"
                          className="-mb-3 font-medium">
                Email
              </Typography>
              <Input
                  type="email"
                  size="lg"
                  placeholder="name@email.com"
                  className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
                  labelProps={{
                    className: "before:content-none after:content-none",
                  }}
              />
              <Typography variant="small" color="blue-gray"
                          className="-mb-3 font-medium">
                Password
              </Typography>
              <Input
                  type="password"
                  size="lg"
                  placeholder="********"
                  className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
                  labelProps={{
                    className: "before:content-none after:content-none",
                  }}
              />
            </div>

            <Button className="mt-6" fullWidth type={"submit"}>
              Sign In
            </Button>

            <Typography variant="paragraph"
                        className="text-center text-blue-gray-500 font-medium mt-4">
              계정이 없으신가요?
              <Link to="/auth/sign-up"
                    className="text-gray-900 ml-1">회원가입</Link>
            </Typography>
          </form>

        </div>
        <div className="w-2/5 h-full hidden lg:block">
          <img
              src="/img/pattern.png"
              className="h-full w-full object-cover rounded-3xl"
          />
        </div>

      </section>
  );
}

export default SignIn;
