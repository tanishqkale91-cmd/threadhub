import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Button } from '../common/Button';

export function Navbar() {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header className="sticky top-0 z-50 bg-slate-950/80 backdrop-blur-md border-b border-slate-800/80">
      <div className="max-w-6xl mx-auto px-4 sm:px-6 h-16 flex items-center justify-between">
        {/* Brand Logo */}
        <Link to="/" className="flex items-center gap-2 text-xl font-bold tracking-tight text-white group">
          <span className="w-8 h-8 rounded-lg bg-orange-600 flex items-center justify-center text-white font-extrabold text-lg shadow-sm group-hover:bg-orange-500 transition-colors">
            T
          </span>
          <span>Thread<span className="text-orange-500">Hub</span></span>
        </Link>

        {/* Navigation Actions */}
        <nav className="flex items-center gap-4">
          <Link
            to="/communities"
            className="text-sm font-medium text-slate-300 hover:text-white transition-colors"
          >
            Communities
          </Link>

          {isAuthenticated ? (
            <>
              <Link to="/communities/create">
                <Button variant="primary" size="sm">
                  + Create Community
                </Button>
              </Link>
              <div className="h-4 w-px bg-slate-800 mx-1"></div>
              <span className="text-xs font-semibold text-slate-400 bg-slate-900 border border-slate-800 px-2.5 py-1 rounded-full">
                u/{user?.username}
              </span>
              <Button variant="outline" size="sm" onClick={handleLogout}>
                Logout
              </Button>
            </>
          ) : (
            <>
              <Link to="/login">
                <Button variant="outline" size="sm">
                  Log In
                </Button>
              </Link>
              <Link to="/register">
                <Button variant="primary" size="sm">
                  Sign Up
                </Button>
              </Link>
            </>
          )}
        </nav>
      </div>
    </header>
  );
}
