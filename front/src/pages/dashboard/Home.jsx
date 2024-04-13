import React, { useEffect, useState } from 'react';
import ActionCard from '@/widgets/cards/AutionCard.jsx';
import { getAuctions } from '@/api/auction.js';

export function Home() {
  const [auctions, setAuctions] = useState([...auctionList]);

  useEffect(() => {
    getAuctions().then((response) => {
      console.log('auctions', response.data);
      // setAuctions(response.data.data.content);
    });
  }, []);
  return (
    <div className="mt-12">
      <div className="mb-12 grid gap-x-6 gap-y-10 md:grid-cols-2 xl:grid-cols-4">
        {auctions.map((auction, i) => (
          <ActionCard
            key={i}
            auctionId={auction.auctionId}
            status={auction.status}
            title={auction.title}
            filePath={auction.filePath}
            finishedAt={auction.finishedAt}
            footer={true}
          />
        ))}
      </div>
    </div>
  );
}

export default Home;

const auctionList = [
  {
    auctionId: 1,
    title: 'Vintage Camera Auction',
    status: 'ON_SALE',
    filePath: 'https://source.unsplash.com/1600x900/?camera',
    finishedAt: new Date('2024-05-20T15:00:00'),
  },
  {
    auctionId: 2,
    title: 'Classic Car Auction',
    status: 'HOLD',
    filePath: 'https://source.unsplash.com/1600x900/?camera',
    finishedAt: new Date('2024-04-15T15:00:00'),
  },
  {
    auctionId: 3,
    title: 'Rare Coins Auction',
    status: 'COMPLETED',
    filePath: 'https://source.unsplash.com/1600x900/?camera',
    finishedAt: new Date('2024-03-01T15:00:00'),
  },
];
