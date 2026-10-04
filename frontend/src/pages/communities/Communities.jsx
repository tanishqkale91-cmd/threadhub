import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { communityApi } from '../../api/communityApi';
import { useAuth } from '../../context/AuthContext';
import { Button } from '../../components/common/Button';
import { Loading } from '../../components/common/Loading';
import { ErrorMessage } from '../../components/common/ErrorMessage';

export function Communities() {
  const [communities, setCommunities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    fetchCommunities();
  }, []);

  const fetchCommunities = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await communityApi.getAllCommunities();
      setCommunities(data || []);
    } catch (err) {
      setError(err.message || 'Unable to load communities.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-4 border-b border-slate-800">
        <div>
          <h1 className="text-2xl font-bold text-white tracking-tight">Communities</h1>
          <p className="text-xs text-slate-400 mt-1">Explore topic-based discussion groups</p>
        </div>
        {isAuthenticated && (
          <Link to="/communities/create">
            <Button variant="primary" size="sm">
              + Create Community
            </Button>
          </Link>
        )}
      </div>

      {/* State Handling */}
      {loading && <Loading message="Loading communities..." />}

      {error && <ErrorMessage title="Failed to load communities" message={error} />}

      {!loading && !error && communities.length === 0 && (
        <div className="text-center py-12 bg-slate-900/50 border border-slate-800/80 rounded-xl space-y-3">
          <p className="text-sm font-medium text-slate-400">No communities found.</p>
          {isAuthenticated && (
            <Link to="/communities/create">
              <Button variant="outline" size="sm">
                Be the first to create one
              </Button>
            </Link>
          )}
        </div>
      )}

      {/* Communities Grid */}
      {!loading && !error && communities.length > 0 && (
        <div className="grid md:grid-cols-2 gap-4">
          {communities.map((comm) => (
            <Link
              key={comm.id}
              to={`/communities/${comm.id}`}
              className="group block p-5 rounded-xl bg-slate-900 border border-slate-800 hover:border-orange-500/50 transition-all duration-200 shadow-sm"
            >
              <div className="flex items-start justify-between gap-2 mb-2">
                <h2 className="text-lg font-bold text-white group-hover:text-orange-400 transition-colors">
                  t/{comm.name}
                </h2>
                <span className="text-xs font-medium text-slate-400 bg-slate-800 px-2 py-0.5 rounded-full shrink-0">
                  {comm.memberCount || 1} {comm.memberCount === 1 ? 'member' : 'members'}
                </span>
              </div>
              <p className="text-xs text-slate-400 line-clamp-2 mb-4 leading-relaxed">
                {comm.description || 'No description provided.'}
              </p>
              <div className="text-[11px] text-slate-500 flex items-center justify-between border-t border-slate-800/60 pt-3">
                <span>Owner: <strong className="text-slate-400">u/{comm.owner?.username || 'unknown'}</strong></span>
                <span>Created {new Date(comm.createdAt).toLocaleDateString()}</span>
              </div>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}
