import React, { useEffect, useState } from 'react';
import {
  Card,
  CardBody,
  CardFooter,
  CardHeader,
  Typography,
} from '@material-tailwind/react';
import {useNavigate} from "react-router-dom";

export function ActionCard({
  auctionId,
  status,
  title,
  filePath,
  value,
  finishedAt,
  footer,
}) {
  const navigate = useNavigate();
  const [remainingTime, setRemainingTime] = useState('');

  useEffect(() => {
    const updateRemainingTime = () => {
      if (status === 'ON_SALE') {
        const now = new Date();
        const end = new Date(finishedAt);
        const diff = end - now;
        if (diff > 0) {
          const hours = Math.floor(diff / (1000 * 60 * 60));
          const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
          const seconds = Math.floor((diff % (1000 * 60)) / 1000);
          setRemainingTime(`${hours}시간 ${minutes}분 ${seconds}초 남음`);
        } else {
          setRemainingTime('경매 종료');
        }
      } else if (status === 'COMPLETED') {
        const endDate = new Date(finishedAt).toLocaleString('ko-KR', {
          year: 'numeric',
          month: 'long',
          day: 'numeric',
          hour: 'numeric',
          hour12: false,
        });
        setRemainingTime(`${endDate} 종료`);
      }
    };

    const timer = setInterval(updateRemainingTime, 1000);
    return () => clearInterval(timer);
  }, [status, finishedAt]);

  return (
    <Card
        className="border border-blue-gray-100 shadow-sm"
        onClick={() => {
          navigate(`/dashboard/auction/${auctionId}`);
        }}
    >
      <div className="flex justify-between">
        <CardHeader
          variant="gradient"
          color={'blue-gray'}
          floated={false}
          shadow={false}
          className="h-24 w-24 place-items-center"
        >
          <img
            src={filePath}
            alt={`${auctionId}-${title}-image`}
            className="h-full w-full object-cover"
          />
        </CardHeader>
        <CardBody className="p-4 text-right">
          <Typography variant="h5" className="text-blue-gray-600">
            {title}
          </Typography>
          <Typography
            variant="small"
            className={`mb-5 font-bold ${statusColors[status]}`}
          >
            {statusLabels[status]}
          </Typography>
          <Typography variant="h6" color="blue-gray" className="self-baseline">
            {status === 'HOLD' ? 'ㅤ' : remainingTime}
          </Typography>
        </CardBody>
      </div>
      {footer && (
        <CardFooter className="border-t border-blue-gray-50 p-4 text-right">
          {`${Number(value || 0).toLocaleString('ko-KR')} 원`}
        </CardFooter>
      )}
    </Card>
  );
}

export default ActionCard;

const statusLabels = {
  ON_SALE: '진행 중',
  HOLD: '진행 예정',
  COMPLETED: '완료',
};

const statusColors = {
  ON_SALE: 'text-green-400',
  HOLD: 'text-gray-500',
  COMPLETED: 'text-red-400',
};
