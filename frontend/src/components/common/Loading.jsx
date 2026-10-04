export function Loading({ message = 'Loading...' }) {
  return (
    <div className="flex flex-col items-center justify-center py-12 px-4 text-center">
      <div className="relative w-10 h-10 mb-3">
        <div className="absolute top-0 left-0 w-full h-full border-4 border-orange-500/20 rounded-full"></div>
        <div className="absolute top-0 left-0 w-full h-full border-4 border-orange-500 border-t-transparent rounded-full animate-spin"></div>
      </div>
      <p className="text-sm font-medium text-slate-400">{message}</p>
    </div>
  );
}
