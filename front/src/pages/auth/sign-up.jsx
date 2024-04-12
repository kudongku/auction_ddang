import {Input, Button, Typography} from "@material-tailwind/react";
import { Link } from "react-router-dom";
import axios from "axios";
import {useEffect, useState} from "react";
const { kakao } = window;

function SignUp() {
  const [user_lat, setLat] = useState();
  const [user_lon, setLon] = useState();

  const res = axios.get('https://geolocation-db.com/json/')
  .then((res) => {
    setLat(res.data.latitude);
    setLon(res.data.longitude);
    console.log("data : ", res)
  })

  const [addressTest, setAddress] = useState();

  const [formData, setFormData] = useState({
    email: "",
    nickname: "",
    password: "",
    passwordConfirm : "",
    address : addressTest
  });

  const handleChange = (e) => {
    console.log(e.target.name)
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    alert(formData.address);
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

  useEffect(() => {
    const container = document.getElementById('map');
    const options = {
      center  : new kakao.maps.LatLng(user_lat, user_lon), // todo ip로 위도와 경도 받기
      level : 3
    }
    const map = new kakao.maps.Map(container, options);

    const geocoder = new kakao.maps.services.Geocoder();
    const marker = new kakao.maps.Marker();
    const infowindow = new kakao.maps.InfoWindow({ zindex: 1 });

    const searchAddrFromCoords = (coords, callback) => {
      geocoder.coord2RegionCode(coords.getLng(), coords.getLat(), callback);
    };

    const searchDetailAddrFromCoords = (coords, callback) => {
      geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
    };

    const displayCenterInfo = (result, status) => {
      if (status === kakao.maps.services.Status.OK) {
        const infoDiv = document.getElementById('centerAddr');
        for (let i = 0; i < result.length; i++) {
          if (result[i].region_type === 'H') {
            infoDiv.innerHTML = result[i].address_name;
            setAddress(result[i].address_name);
            break;
          }
        }
      }
    };

    const handleMapClick = (mouseEvent) => {
      searchAddrFromCoords(mouseEvent.latLng, displayCenterInfo)
      searchDetailAddrFromCoords(mouseEvent.latLng, (result, status) => {
        if (status === kakao.maps.services.Status.OK) {
          let detailAddr = !!result[0].road_address ? '<div>도로명주소 : ' + result[0].road_address.address_name + '</div>' : '';
          detailAddr += '<div>지번 주소 : ' + result[0].address.address_name + '</div>';

          const content = '<div class="bAddr">' +
              '<span class="title">법정동 주소정보</span>' +
              detailAddr +
              '</div>';

          marker.setPosition(mouseEvent.latLng);
          marker.setMap(map);

          infowindow.setContent(content);
          infowindow.open(map, marker);
        }
      });
    };

    map.addListener('click', handleMapClick);

  }, );

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

            <div className="map_wrap">
              <div className="hAddr">
                <div className="title">주소정보</div>
                <div id="centerAddr"></div>
              </div>
              <div id="map" className={"mb-1 flex flex-col gap-6"} style={{ width: "auto", height: '300px', position: 'relative', overflow: 'hidden' }}></div>
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