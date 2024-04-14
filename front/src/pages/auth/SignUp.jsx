import { Button, Input, Typography } from '@material-tailwind/react';
import axios from 'axios';
import { useEffect, useRef, useState } from 'react';
import { signup } from '@/api/user.js';
import { useNavigate } from 'react-router-dom';

const { kakao } = window;

function SignUp() {
  const navigate = useNavigate();
  const [userCoord, setUserCoord] = useState();
  const [formData, setFormData] = useState({
    email: '',
    nickname: '',
    password: '',
    passwordConfirm: '',
    address: '서울특별시',
  });
  const kakaoMapRef = useRef();

  useEffect(() => {
    axios.get('https://geolocation-db.com/json/').then((res) => {
      setUserCoord({ lat: res.data.latitude, lon: res.data.longitude });
    });
  }, []);

  useEffect(() => {
    if (!userCoord) {
      return;
    }
    initializeKakaoMap();
  }, [userCoord]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await signup(formData); // 회원가입 API 호출
      console.log('response', response.data);
      console.log('response.status', response.status);
      if (response.status === 201) {
        navigate('/auth/sign-in');
        alert('환영해요! 로그인해주세요.');
      } else {
        alert(response.data.message);
      }
    } catch (error) {
      console.error('Registration failed:', error);
      alert('회원가입에 실패했어요.\n' + error);
    }
  };

  const initializeKakaoMap = () => {
    const container = document.getElementById('map');
    const options = {
      center: new kakao.maps.LatLng(userCoord.lat, userCoord.lon),
      level: 3,
    };
    const map = new kakao.maps.Map(container, options);

    const geocoder = new kakao.maps.services.Geocoder();
    const marker = new kakao.maps.Marker();
    const infoWindow = new kakao.maps.InfoWindow({ zindex: 1 });

    map.addListener('click', handleMapClick);

    kakaoMapRef.current = { map, geocoder, marker, infoWindow };
  };

  const handleMapClick = (mouseEvent) => {
    searchAddrFromCoords(mouseEvent.latLng, (result, status) => {
      if (status === kakao.maps.services.Status.OK) {
        const infoDiv = document.getElementById('centerAddr');
        for (let i = 0; i < result.length; i++) {
          if (result[i].region_type === 'H') {
            infoDiv.innerHTML = result[i].address_name;
            setFormData({ ...formData, address: result[i].address_name });
            break;
          }
        }
      }
    });
    //   searchDetailAddrFromCoords(mouseEvent.latLng, (result, status) => {
    //     if (status === kakao.maps.services.Status.OK) {
    //       let detailAddr = !!result[0].road_address ? '<div>도로명주소 : ' + result[0].road_address.address_name + '</div>' : '';
    //       detailAddr += '<div>지번 주소 : ' + result[0].address.address_name + '</div>';
    //
    //       const content = '<div class="bAddr">' +
    //           '<span class="title">법정동 주소정보</span>' +
    //           detailAddr +
    //           '</div>';
    //
    //
    //     }
    //   });
  };

  const searchAddrFromCoords = (coords, callback) => {
    kakaoMapRef.current.geocoder.coord2RegionCode(
      coords.getLng(),
      coords.getLat(),
      callback,
    );
    kakaoMapRef.current.marker.setPosition(coords);
    kakaoMapRef.current.marker.setMap(kakaoMapRef.current.map);

    // kakaoMapRef.current.infowindow.setContent(content);
    // kakaoMapRef.current.infowindow.open(kakaoMapRef.current.map, kakaoMapRef.current.marker);
  };

  return (
    <section className="m-8 flex">
      <div className="hidden h-full w-2/5 lg:block">
        <img
          src="/img/pattern.png"
          className="h-full w-full rounded-3xl object-cover"
        />
      </div>
      <div className="flex w-full flex-col items-center justify-center lg:w-3/5">
        <div className="text-center">
          <Typography variant="h2" className="mb-4 font-bold">
            Sign Up
          </Typography>
          <Typography
            variant="paragraph"
            color="blue-gray"
            className="text-lg font-normal"
          >
            이메일, 닉네임, 비밀번호를 입력해주세요
          </Typography>
        </div>
        <form
          className="mx-auto mb-2 mt-8 w-80 max-w-screen-lg lg:w-1/2"
          onSubmit={handleSubmit}
        >
          <div className="mb-1 flex flex-col gap-6">
            <Typography
              variant="small"
              color="blue-gray"
              className="-mb-3 font-medium"
            >
              이메일
            </Typography>
            <Input
              size="lg"
              placeholder="name@mail.com"
              className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
              labelProps={{
                className: 'before:content-none after:content-none',
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
                className: 'before:content-none after:content-none',
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
                className: 'before:content-none after:content-none',
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
                className: 'before:content-none after:content-none',
              }}
              name="passwordConfirm"
              value={formData.passwordConfirm}
              onChange={handleChange}
            />
          </div>

          <div className="map_wrap">
            <div className="hAddr">
              <div className="title">주소정보</div>
              <div id="centerAddr"></div>
            </div>
            <div
              id="map"
              className={'mb-1 flex flex-col gap-6'}
              style={{
                width: 'auto',
                height: '300px',
                position: 'relative',
                overflow: 'hidden',
              }}
            ></div>
          </div>

          <Button className="mt-6" fullWidth type={'submit'}>
            등록하기
          </Button>
        </form>
      </div>
    </section>
  );
}

export default SignUp;
