export function Input({
  label,
  id,
  type = 'text',
  error,
  helperText,
  required = false,
  className = '',
  ...props
}) {
  return (
    <div className="w-full space-y-1.5">
      {label && (
        <label htmlFor={id} className="block text-xs font-semibold uppercase tracking-wider text-slate-400">
          {label} {required && <span className="text-orange-500">*</span>}
        </label>
      )}
      <input
        id={id}
        type={type}
        required={required}
        className={`w-full rounded-lg bg-slate-900 border ${
          error ? 'border-rose-500 focus:ring-rose-500' : 'border-slate-800 focus:border-orange-500 focus:ring-orange-500'
        } px-3.5 py-2.5 text-sm text-slate-100 placeholder-slate-500 focus:outline-none focus:ring-1 transition-colors duration-150 ${className}`}
        {...props}
      />
      {error && <p className="text-xs text-rose-400 font-medium">{error}</p>}
      {!error && helperText && <p className="text-xs text-slate-500">{helperText}</p>}
    </div>
  );
}
