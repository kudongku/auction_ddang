import React, {useEffect, useRef, useState} from 'react';
import {getAuctions} from '@/api/auction.js';
import ActionCard from '@/widgets/cards/AutionCard.jsx';
import {LoadingSpinner} from '@/common/LoadingSpinner.jsx';
import {useSearch} from '@/context/search-context.jsx';
import {useNavigate} from "react-router-dom";

export function Home() {
  const navigate = useNavigate();
  const [auctions, setAuctions] = useState([]);
  const [statusFilter, setStatusFilter] = useState('');
  const {search, setSearch} = useSearch();
  const [isLoading, setIsLoading] = useState(false);
  // 다음 페이지가 있는지 여부
  const [hasNextPage, setHasNextPage] = useState(true);
  // 현재 페이지 번호
  const [pageNumber, setPageNumber] = useState(0);
  const pageSize = 10;

  const loader = useRef(null);
  const debounceTimer = useRef(null);

  //스크롤 감지 -> 다음페이지 로드하기
  // const handleObserver = (entities) => {
  //   const target = entities[0];
  //   if (target.isIntersecting && !isLoading && hasNextPage) {
  //     setPageNumber((prevPageNumber) => prevPageNumber + 1);
  //   }
  // };
  //
  // useEffect(() => {
  //   const option = {
  //     root: null,
  //     rootMargin: '20px',
  //     threshold: 0,
  //   };
  //   const observer = new IntersectionObserver(handleObserver, option);
  //   if (loader.current && hasNextPage) {
  //     observer.observe(loader.current);
  //   }
  //
  //   return () => {
  //     if (loader.current) {
  //       observer.unobserve(loader.current);
  //     }
  //   };
  // }, [isLoading, hasNextPage]);

  // Search에 대한 Debouncing 처리
  useEffect(() => {
    if (debounceTimer.current) {
      clearTimeout(debounceTimer.current);
    }
    // debounceTimer.current = setTimeout(() => {
    //   fetchAuctions();
    // }, 500);
    debounceTimer.current = setTimeout(() => {
      setPageNumber(0);
      fetchAuctions(0);
    }, 500);

    return () => {
      if (debounceTimer.current) {
        clearTimeout(debounceTimer.current);
      }
    };
  }, [search]);

  //초기 렌더링 할때 경매 아이템 가져오기
  useEffect(() => {
    fetchAuctions(pageNumber);
  }, [statusFilter, pageNumber]);

  const fetchAuctions = (page) => {
    setIsLoading(true);
    if (page === 0) {
      setAuctions([]);
    }
    getAuctions({
      status: statusFilter,
      title: search || null,
      page,
      pageSize
    })
    .then((response) => {
      if (response.data.length === 0) {
        //만약 다음페이지가 없으면 false
        setHasNextPage(false);
      } else {
        setAuctions(response.data.data);
      }
      // if (page === 0) {
      //   setAuctions(response.data.data.content);
      // } else {
      //   setAuctions((prev) => [...prev, ...response.data.data.content]);
      // }
    })
    .catch((error) => {
      console.error('Failed to fetch auctions:', error);
    })
    .finally(() => {
      setIsLoading(false);
    });
  };

  useEffect(() => {
    setPageNumber(0);
    setAuctions([]);
  }, [statusFilter, search]);

  return (
      <div className="mt-2">
        <div className="mb-4 flex gap-2 text-[14px] font-semibold">
          {['전체', '경매 중', '경매 마감', '경매 완료'].map((label, index) => {
            const status =
                label === '경매 중'
                    ? 'ON_SALE'
                    : label === '경매 마감'
                        ? 'HOLD'
                        : label === '경매 완료'
                            ? 'COMPLETED'
                            : '';
            return (
                <button
                    key={index}
                    onClick={() => {
                      setPageNumber(0);
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
          <button
              onClick={()=>{
                navigate(`/dashboard/createAuction`);
              }}
              className={`rounded-md px-4 py-2 text-white bg-red-600 hover:opacity-90'}`}
          >
            경매올리기
          </button>
        </div>
        <div className="mb-12 flex flex-col gap-x-6 gap-y-10 md:grid-cols-2">
          {isLoading && <LoadingSpinner/>}
          {auctions.map((auction, i) => (
              <ActionCard
                  key={i}
                  auctionId={auction.auctionId}
                  status={auction.status}
                  title={auction.title}
                  filePath={auction.filePath}
                  finishedAt={auction.finishedAt}
                  value={auction.price}
                  footer={true}
              />
          ))}
          {hasNextPage && (<div ref={loader}/>)}
          {/* Observer Target */}
        </div>
      </div>
  );
}

export default Home;

