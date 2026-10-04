export function ErrorMessage({ title = 'Something went wrong', message, details = [] }) {
  if (!message && details.length === 0) return null;

  return (
    <div className="rounded-lg border border-rose-500/30 bg-rose-950/20 p-4 text-rose-300 my-4 shadow-sm">
      <div className="flex items-start gap-3">
        <svg className="w-5 h-5 text-rose-400 mt-0.5 shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
        </svg>
        <div>
          <h4 className="text-sm font-semibold text-rose-200">{title}</h4>
          {message && <p className="text-xs text-rose-300 mt-1">{message}</p>}
          {details && details.length > 0 && (
            <ul className="list-disc list-inside text-xs text-rose-300 mt-2 space-y-1">
              {details.map((item, idx) => (
                <li key={idx}>{item}</li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
}
