import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Button } from '../components/common/Button';

export function Home() {
  const { isAuthenticated } = useAuth();

  return (
    <div className="py-12 space-y-16">
      {/* Hero Section */}
      <section className="text-center max-w-3xl mx-auto space-y-6">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-orange-500/10 border border-orange-500/20 text-orange-400 text-xs font-semibold">
          <span>ThreadHub Phase 3</span>
          <span className="text-slate-600">•</span>
          <span>Community Discussion Engine</span>
        </div>
        
        <h1 className="text-4xl sm:text-5xl font-extrabold tracking-tight text-white leading-tight">
          Where discussions thrive in <span className="text-orange-500">focused communities</span>.
        </h1>
        
        <p className="text-lg text-slate-400 leading-relaxed">
          ThreadHub connects people around shared interests. Discover communities, start discussions, and engage with member-driven topic hubs.
        </p>

        <div className="flex flex-wrap items-center justify-center gap-4 pt-4">
          <Link to="/communities">
            <Button variant="primary" size="lg">
              Browse Communities
            </Button>
          </Link>
          
          {isAuthenticated ? (
            <Link to="/communities/create">
              <Button variant="secondary" size="lg">
                Create a Community
              </Button>
            </Link>
          ) : (
            <Link to="/register">
              <Button variant="outline" size="lg">
                Join ThreadHub
              </Button>
            </Link>
          )}
        </div>
      </section>

      {/* Feature Cards */}
      <section className="grid md:grid-cols-3 gap-6 pt-6">
        <div className="p-6 rounded-xl bg-slate-900 border border-slate-800 space-y-3">
          <div className="w-10 h-10 rounded-lg bg-orange-600/20 text-orange-400 flex items-center justify-center font-bold text-lg">
            #
          </div>
          <h3 className="text-lg font-bold text-white">Topic Communities</h3>
          <p className="text-sm text-slate-400">
            Create or join dedicated spaces built around specific interests, technologies, and discussions.
          </p>
        </div>

        <div className="p-6 rounded-xl bg-slate-900 border border-slate-800 space-y-3">
          <div className="w-10 h-10 rounded-lg bg-orange-600/20 text-orange-400 flex items-center justify-center font-bold text-lg">
            &lt;/&gt;
          </div>
          <h3 className="text-lg font-bold text-white">Member Ownership</h3>
          <p className="text-sm text-slate-400">
            Community creators automatically assume owner status to oversee community growth and member lists.
          </p>
        </div>

        <div className="p-6 rounded-xl bg-slate-900 border border-slate-800 space-y-3">
          <div className="w-10 h-10 rounded-lg bg-orange-600/20 text-orange-400 flex items-center justify-center font-bold text-lg">
            &check;
          </div>
          <h3 className="text-lg font-bold text-white">Secure JWT Auth</h3>
          <p className="text-sm text-slate-400">
            Stateless OAuth2 resource server architecture ensuring secure membership actions and identity token protection.
          </p>
        </div>
      </section>
    </div>
  );
}
