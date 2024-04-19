import {
  HomeIcon,
  InformationCircleIcon,
  RectangleStackIcon,
  ServerStackIcon,
  TableCellsIcon,
  UserCircleIcon,
} from '@heroicons/react/24/solid';
import {
  AuctionDetails,
  Home,
  Notifications,
  Profile,
  Tables
} from '@/pages/dashboard';
import SignIn from '@/pages/auth/SignIn.jsx';
import SignUp from '@/pages/auth/SignUp.jsx';
import HomeReference from "@/pages/dashboard/home-reference.jsx";
import CreateAuction from "@/pages/dashboard/CreateAuction.jsx";

const icon = {
  className: 'w-5 h-5 text-inherit',
};

export const routes = [
  {
    layout: 'dashboard',
    pages: [
      {
        icon: <HomeIcon {...icon} />,
        name: 'dashboard',
        path: '/home',
        element: <Home/>,
      },
      {
        icon: <HomeIcon {...icon} />,
        name: 'dashboard2',
        path: '/home-reference',
        element: <HomeReference/>,
      },
      {
        icon: <UserCircleIcon {...icon} />,
        name: 'auction',
        path: '/auction/:auctionId',
        element: <AuctionDetails/>,
      },
      {
        icon: <UserCircleIcon {...icon} />,
        name: 'createAuction',
        path: '/createAuction',
        element: <CreateAuction/>,
      },
      {
        icon: <UserCircleIcon {...icon} />,
        name: 'profile',
        path: '/profile',
        element: <Profile/>,
      },
      {
        icon: <TableCellsIcon {...icon} />,
        name: 'tables',
        path: '/tables',
        element: <Tables/>,
      },
      {
        icon: <InformationCircleIcon {...icon} />,
        name: 'notifications',
        path: '/notifications',
        element: <Notifications/>,
      },
    ],
  },
  {
    title: 'auth pages',
    layout: 'auth',
    pages: [
      {
        icon: <ServerStackIcon {...icon} />,
        name: 'sign in',
        path: '/sign-in',
        element: <SignIn/>,
      },
      {
        icon: <RectangleStackIcon {...icon} />,
        name: 'sign up',
        path: '/sign-up',
        element: <SignUp/>,
      },
    ],
  },
];

export default routes;
