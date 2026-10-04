import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Button } from '../../components/common/Button';
import { Input } from '../../components/common/Input';
import { ErrorMessage } from '../../components/common/ErrorMessage';

export function Register() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [details, setDetails] = useState([]);

  const { register, login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setDetails([]);

    if (username.length < 3) {
      setError('Username must be at least 3 characters');
      return;
    }
    if (password.length < 6) {
      setError('Password must be at least 6 characters');
      return;
    }

    setLoading(true);

    try {
      await register(username, email, password);
      // Auto login after successful registration
      await login(email, password);
      navigate('/communities');
    } catch (err) {
      setError(err.message || 'Registration failed');
      if (err.data?.details) {
        setDetails(err.data.details);
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto py-12">
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6 sm:p-8 shadow-xl space-y-6">
        <div className="space-y-2 text-center">
          <h1 className="text-2xl font-extrabold text-white tracking-tight">Create an Account</h1>
          <p className="text-xs text-slate-400">Join ThreadHub to start and participate in communities</p>
        </div>

        {error && <ErrorMessage message={error} details={details} />}

        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Username"
            id="username"
            type="text"
            required
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="johndoe"
            helperText="3 to 50 characters, unique"
          />

          <Input
            label="Email Address"
            id="email"
            type="email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="you@example.com"
          />

          <Input
            label="Password"
            id="password"
            type="password"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="••••••••"
            helperText="At least 6 characters"
          />

          <Button
            type="submit"
            variant="primary"
            className="w-full mt-2"
            isLoading={loading}
          >
            Create Account
          </Button>
        </form>

        <div className="text-center text-xs text-slate-400 pt-2 border-t border-slate-800">
          Already have an account?{' '}
          <Link to="/login" className="text-orange-400 hover:text-orange-300 font-semibold underline">
            Log in
          </Link>
        </div>
      </div>
    </div>
  );
}
