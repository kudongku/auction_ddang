export function LoadingSpinner() {
  return (
    <div className="flex items-center justify-center p-40">
      <div className="h-12 w-12 animate-spin rounded-full border-y-2 border-solid border-blue-gray-100 border-t-transparent shadow-md"></div>
    </div>
  );
}
