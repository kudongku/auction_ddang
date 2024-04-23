import PropTypes from 'prop-types';
import { Typography } from '@material-tailwind/react';
import { HeartIcon } from '@heroicons/react/24/solid';

export function Footer({ brandName, routes }) {
  const year = new Date().getFullYear();

  return (
    <footer className="py-2">
      <div className="flex w-full flex-wrap items-center justify-center gap-6 px-2 md:justify-between">
        <Typography variant="small" className="font-normal text-inherit">
          &copy; {year}, made with{' '}
          <HeartIcon className="-mt-0.5 inline-block h-3.5 w-3.5 text-red-600" />{' '}
          by{' '}
          <span className="font-bold transition-colors hover:text-blue-500">
            {brandName}
          </span>{' '}
          for a better world.
        </Typography>
        <ul className="flex items-center gap-4">
          {routes.map(({ name, path }) => (
            <li key={name}>
              <Typography
                as="a"
                href={path}
                target="_blank"
                variant="small"
                className="px-1 py-0.5 font-normal text-inherit transition-colors hover:text-blue-500"
              >
                {name}
              </Typography>
            </li>
          ))}
        </ul>
      </div>
    </footer>
  );
}

Footer.defaultProps = {
  brandName: "I's Protocol",
  routes: [
    { name: 'GitHub', path: 'https://github.com/IP-I-s-Protocol/DDang' },
    { name: 'Blog', path: 'https://velog.io/@kudongku/posts' },
    {
      name: 'License',
      path: 'https://github.com/IP-I-s-Protocol/DDang/blob/dev/LICENSE',
    },
  ],
};

Footer.propTypes = {
  brandName: PropTypes.string,
  brandLink: PropTypes.string,
  routes: PropTypes.arrayOf(PropTypes.object),
};

Footer.displayName = '/src/widgets/layout/footer.jsx';

export default Footer;
