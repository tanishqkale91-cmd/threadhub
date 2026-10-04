import { Outlet } from 'react-router-dom';
import { Navbar } from './Navbar';

export function AppLayout() {
  return (
    <div className="min-h-screen flex flex-col bg-slate-950 text-slate-100">
      <Navbar />
      <main className="flex-1 max-w-6xl w-full mx-auto px-4 sm:px-6 py-8">
        <Outlet />
      </main>
      <footer className="border-t border-slate-900 py-6 text-center text-xs text-slate-500">
        ThreadHub &copy; {new Date().getFullYear()} &mdash; Community Discussion Platform
      </footer>
    </div>
  );
}
