import React, { useEffect, useRef, useState } from 'react';
import { getAuctions } from '@/api/auction.js';
import ActionCard from '@/widgets/cards/AutionCard.jsx';
import { LoadingSpinner } from '@/common/LoadingSpinner.jsx';

export function Home() {
  const [auctions, setAuctions] = useState([]);
  const [page, setPage] = useState(0);
  const [statusFilter, setStatusFilter] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const loader = useRef(null);

  const handleObserver = (entities) => {
    const target = entities[0];
    if (target.isIntersecting) {
      setPage((prev) => prev + 1);
    }
  };

  useEffect(() => {
    const option = {
      root: null,
      rootMargin: '20px',
      threshold: 0,
    };
    const observer = new IntersectionObserver(handleObserver, option);
    if (loader.current) observer.observe(loader.current);

    return () => {
      if (loader.current) observer.unobserve(loader.current);
    };
  }, []);

  useEffect(() => {
    setIsLoading(true);
    getAuctions({
      status: statusFilter,
      title: '', // 여기에 Search 넣어야함
      page,
      size: 10,
    })
      .then((response) => {
        if (page === 0) {
          setAuctions(response.data.data.content);
        } else {
          setAuctions((prev) => [...prev, ...response.data.data.content]);
        }
      })
      .catch((error) => {
        console.error('Failed to fetch auctions:', error);
      })
      .finally(() => {
        setIsLoading(false);
      });
  }, [page, statusFilter]);

  useEffect(() => {
    setAuctions([]);
  }, [statusFilter]);

  return (
    <div className="mt-2">
      <div className="mb-4 flex gap-2 text-[14px] font-semibold">
        {['전체', '진행 중', '진행 예정', '완료'].map((label, index) => {
          const status =
            label === '진행 중'
              ? 'ON_SALE'
              : label === '진행 예정'
              ? 'HOLD'
              : label === '완료'
              ? 'COMPLETED'
              : '';
          return (
            <button
              key={index}
              onClick={() => {
                setPage(0);
                setStatusFilter(status);
              }}
              className={`rounded-full px-4 py-2 text-white ${
                statusFilter === status
                  ? 'bg-blue-gray-600 hover:opacity-90'
                  : 'bg-blue-gray-200 hover:bg-blue-gray-600'
              }`}
            >
              {label}
            </button>
          );
        })}
      </div>
      <div className="mb-12 flex flex-col gap-x-6 gap-y-10 md:grid-cols-2">
        {isLoading && <LoadingSpinner />}
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
        <div ref={loader} /> {/* Observer Target */}
      </div>
    </div>
  );
}

export default Home;
