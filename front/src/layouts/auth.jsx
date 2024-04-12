import {
  ArrowRightOnRectangleIcon,
  ChartPieIcon,
  UserIcon,
  UserPlusIcon,
} from "@heroicons/react/24/solid";
import routes from "@/routes";
import {Outlet} from "react-router-dom";

export function Auth() {
  const navbarRoutes = [
    {
      name: "dashboard",
      path: "/dashboard/home",
      icon: ChartPieIcon,
    },
    {
      name: "profile",
      path: "/dashboard/home",
      icon: UserIcon,
    },
    {
      name: "sign up",
      path: "/auth/sign-up",
      icon: UserPlusIcon,
    },
    {
      name: "sign in",
      path: "/auth/sign-in",
      icon: ArrowRightOnRectangleIcon,
    },
  ];

  console.log(routes);
  return (
      <Outlet/>
  );
}

Auth.displayName = "/src/layout/Auth.jsx";

export default Auth;
