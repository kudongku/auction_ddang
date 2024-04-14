import { Button, Input, Typography } from '@material-tailwind/react';
import { Link, useNavigate } from 'react-router-dom';
import { signin } from '@/api/user.js';
import { Axios } from '@/configs/axios.js';

function SignIn() {
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    const email = event.target[0].value;
    const password = event.target[1].value;

    try {
      const response = await signin(email, password);
      const token = response.headers['authorization'];
      if (token) {
        localStorage.setItem('authorizationToken', token);
        Axios.defaults.headers.common['Authorization'] = token;
        navigate('/dashboard/home');
      }
      return response.data;
    } catch (error) {
      console.error('Login failed:', error);
      alert('로그인에 실패했어요.\n' + error);
    }
  };

  return (
    <section className="m-8 flex gap-4">
      <div className="mt-24 w-full lg:w-3/5">
        <div className="text-center">
          <Typography variant="h2" className="mb-4 font-bold">
            Sign In
          </Typography>
          <Typography
            variant="paragraph"
            color="blue-gray"
            className="text-lg font-normal"
          >
            이메일과 비밀번호를 입력해주세요.
          </Typography>
        </div>
        <form
          onSubmit={handleSubmit}
          className="mx-auto mb-2 mt-8 w-80 max-w-screen-lg lg:w-1/2"
        >
          <div className="mb-1 flex flex-col gap-6">
            <Typography
              variant="small"
              color="blue-gray"
              className="-mb-3 font-medium"
            >
              Email
            </Typography>
            <Input
              type="email"
              size="lg"
              placeholder="name@email.com"
              className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
              labelProps={{
                className: 'before:content-none after:content-none',
              }}
            />
            <Typography
              variant="small"
              color="blue-gray"
              className="-mb-3 font-medium"
            >
              Password
            </Typography>
            <Input
              type="password"
              size="lg"
              placeholder="********"
              className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
              labelProps={{
                className: 'before:content-none after:content-none',
              }}
            />
          </div>

          <Button className="mt-6" fullWidth type={'submit'}>
            Sign In
          </Button>

          <Typography
            variant="paragraph"
            className="mt-4 text-center font-medium text-blue-gray-500"
          >
            계정이 없으신가요?
            <Link to="/auth/sign-up" className="ml-1 text-gray-900">
              회원가입
            </Link>
          </Typography>
        </form>
      </div>
      <div className="hidden h-full w-2/5 lg:block">
        <img
          src="/img/pattern.png"
          alt={'pattern'}
          className="h-full w-full rounded-3xl object-cover"
        />
      </div>
    </section>
  );
}

export default SignIn;
