import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { communityApi } from '../../api/communityApi';
import { Button } from '../../components/common/Button';
import { Input } from '../../components/common/Input';
import { ErrorMessage } from '../../components/common/ErrorMessage';

export function CreateCommunity() {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [details, setDetails] = useState([]);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setDetails([]);

    if (!name || name.trim().length < 3) {
      setError('Community name must be at least 3 characters long');
      return;
    }

    setLoading(true);

    try {
      const createdCommunity = await communityApi.createCommunity({
        name: name.trim(),
        description: description.trim(),
      });
      navigate(`/communities/${createdCommunity.id}`);
    } catch (err) {
      setError(err.message || 'Failed to create community');
      if (err.data?.details) {
        setDetails(err.data.details);
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-xl mx-auto space-y-6 py-6">
      <Link to="/communities" className="text-xs font-semibold text-orange-400 hover:text-orange-300 transition-colors inline-flex items-center gap-1">
        &larr; Back to Communities
      </Link>

      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6 sm:p-8 shadow-xl space-y-6">
        <div className="space-y-1">
          <h1 className="text-2xl font-extrabold text-white tracking-tight">Create a Community</h1>
          <p className="text-xs text-slate-400">Establish a new topic hub for discussions</p>
        </div>

        {error && <ErrorMessage message={error} details={details} />}

        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Community Name"
            id="name"
            type="text"
            required
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="e.g. webdev"
            helperText="3 to 50 characters, unique name"
          />

          <div className="w-full space-y-1.5">
            <label htmlFor="description" className="block text-xs font-semibold uppercase tracking-wider text-slate-400">
              Description
            </label>
            <textarea
              id="description"
              rows={4}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="What is this community about?"
              className="w-full rounded-lg bg-slate-900 border border-slate-800 focus:border-orange-500 focus:ring-orange-500 px-3.5 py-2.5 text-sm text-slate-100 placeholder-slate-500 focus:outline-none focus:ring-1 transition-colors duration-150 resize-y"
              maxLength={500}
            />
            <p className="text-xs text-slate-500 text-right">{description.length}/500</p>
          </div>

          <div className="flex items-center justify-end gap-3 pt-2">
            <Link to="/communities">
              <Button variant="outline" type="button">
                Cancel
              </Button>
            </Link>
            <Button variant="primary" type="submit" isLoading={loading}>
              Create Community
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
