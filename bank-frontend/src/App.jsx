import { useEffect, useState } from "react";
import {
  createAccount,
  createTransaction,
  createUser,
  getAccounts,
  getTransactions,
  loginUser,
} from "./services/api";
import "./App.css";

function App() {
  const [currentUser, setCurrentUser] = useState(() => {
    const savedUser = localStorage.getItem("bankUser");
    return savedUser ? JSON.parse(savedUser) : null;
  });

  const [accounts, setAccounts] = useState([]);
  const [transactions, setTransactions] = useState([]);

  const [authMode, setAuthMode] = useState("login");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [accountType, setAccountType] = useState("CHECKING");

  const [selectedAccountId, setSelectedAccountId] = useState("");
  const [transactionType, setTransactionType] = useState("DEPOSIT");
  const [amount, setAmount] = useState("");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  async function loadBankData(userId) {
    try {
      setLoading(true);
      setError("");

      const [allAccounts, allTransactions] = await Promise.all([
        getAccounts(),
        getTransactions(),
      ]);

      const userAccounts = allAccounts.filter(
        (account) => account.userId === userId,
      );

      const userAccountIds = new Set(
        userAccounts.map((account) => account.id),
      );

      const userTransactions = allTransactions.filter((transaction) =>
        userAccountIds.has(transaction.accountId),
      );

      setAccounts(userAccounts);
      setTransactions(userTransactions);

      if (userAccounts.length > 0) {
        setSelectedAccountId((previousId) => {
          const stillExists = userAccounts.some(
            (account) => account.id === previousId,
          );

          return stillExists ? previousId : userAccounts[0].id;
        });
      } else {
        setSelectedAccountId("");
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    if (currentUser?.id) {
      loadBankData(currentUser.id);
    }
  }, [currentUser]);

  async function handleAuthentication(event) {
    event.preventDefault();

    try {
      setLoading(true);
      setError("");
      setMessage("");

      let user;

      if (authMode === "login") {
        user = await loginUser(username, password);
      } else {
        user = await createUser(username, password);
      }

      localStorage.setItem("bankUser", JSON.stringify(user));
      setCurrentUser(user);
      setUsername("");
      setPassword("");
    } catch (err) {
      if (authMode === "login" && err.message.includes("401")) {
        setError("Incorrect username or password.");
      } else if (authMode === "register" && err.message.includes("409")) {
        setError("That username already exists.");
      } else {
        setError(err.message);
      }
    } finally {
      setLoading(false);
    }
  }

  async function handleCreateAccount(event) {
    event.preventDefault();

    try {
      setLoading(true);
      setError("");
      setMessage("");

      await createAccount(currentUser.id, accountType);
      await loadBankData(currentUser.id);

      setMessage(`${accountType} account created.`);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function handleTransaction(event) {
    event.preventDefault();

    const numericAmount = Number(amount);

    if (!selectedAccountId) {
      setError("Create or select an account first.");
      return;
    }

    if (!Number.isFinite(numericAmount) || numericAmount <= 0) {
      setError("Enter an amount greater than zero.");
      return;
    }

    try {
      setLoading(true);
      setError("");
      setMessage("");

      await createTransaction(
        selectedAccountId,
        numericAmount,
        transactionType,
      );

      await loadBankData(currentUser.id);

      setAmount("");
      setMessage(
        `${transactionType === "DEPOSIT" ? "Deposit" : "Withdrawal"} completed.`,
      );
    } catch (err) {
      if (err.message.includes("Declined")) {
        setError("Withdrawal declined because the account has insufficient funds.");
      } else {
        setError(err.message);
      }
    } finally {
      setLoading(false);
    }
  }

  function handleLogout() {
    localStorage.removeItem("bankUser");
    setCurrentUser(null);
    setAccounts([]);
    setTransactions([]);
    setSelectedAccountId("");
    setError("");
    setMessage("");
  }

  if (!currentUser) {
    return (
      <main className="auth-page">
        <section className="auth-card">
          <h1>Spring Bank</h1>
          <p>
            {authMode === "login"
              ? "Sign in to view your accounts."
              : "Create your banking profile."}
          </p>

          <form onSubmit={handleAuthentication}>
            <label>
              Username
              <input
                type="text"
                value={username}
                onChange={(event) => setUsername(event.target.value)}
                required
              />
            </label>

            <label>
              Password
              <input
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                required
              />
            </label>

            {error && <p className="error-message">{error}</p>}

            <button disabled={loading}>
              {loading
                ? "Please wait..."
                : authMode === "login"
                  ? "Log in"
                  : "Register"}
            </button>
          </form>

          <button
            type="button"
            className="text-button"
            onClick={() => {
              setAuthMode(authMode === "login" ? "register" : "login");
              setError("");
            }}
          >
            {authMode === "login"
              ? "Need an account? Register"
              : "Already registered? Log in"}
          </button>
        </section>
      </main>
    );
  }

  return (
    <main className="dashboard">
      <header className="dashboard-header">
        <div>
          <h1>Spring Bank</h1>
          <p>Welcome, {currentUser.username}</p>
        </div>

        <button className="secondary-button" onClick={handleLogout}>
          Log out
        </button>
      </header>

      {error && <p className="error-message alert">{error}</p>}
      {message && <p className="success-message alert">{message}</p>}

      <section className="dashboard-grid">
        <article className="panel accounts-panel">
          <h2>Your accounts</h2>

          {loading && accounts.length === 0 ? (
            <p>Loading accounts...</p>
          ) : accounts.length === 0 ? (
            <p>You do not have any accounts yet.</p>
          ) : (
            <div className="account-list">
              {accounts.map((account) => (
                <div className="account-card" key={account.id}>
                  <div>
                    <span className="account-type">
                      {account.accountType}
                    </span>
                    <p className="account-id">
                      Account ending in {account.id.slice(-4)}
                    </p>
                  </div>

                  <strong>
                    ${Number(account.balance).toFixed(2)}
                  </strong>
                </div>
              ))}
            </div>
          )}
        </article>

        <article className="panel">
          <h2>Open an account</h2>

          <form onSubmit={handleCreateAccount}>
            <label>
              Account type
              <select
                value={accountType}
                onChange={(event) => setAccountType(event.target.value)}
              >
                <option value="CHECKING">Checking</option>
                <option value="SAVINGS">Savings</option>
              </select>
            </label>

            <button disabled={loading}>Create account</button>
          </form>
        </article>

        <article className="panel">
          <h2>Make a transaction</h2>

          <form onSubmit={handleTransaction}>
            <label>
              Account
              <select
                value={selectedAccountId}
                onChange={(event) =>
                  setSelectedAccountId(event.target.value)
                }
                disabled={accounts.length === 0}
              >
                {accounts.length === 0 && (
                  <option value="">No accounts available</option>
                )}

                {accounts.map((account) => (
                  <option key={account.id} value={account.id}>
                    {account.accountType} — $
                    {Number(account.balance).toFixed(2)}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Transaction
              <select
                value={transactionType}
                onChange={(event) =>
                  setTransactionType(event.target.value)
                }
              >
                <option value="DEPOSIT">Deposit</option>
                <option value="WITHDRAWAL">Withdrawal</option>
              </select>
            </label>

            <label>
              Amount
              <input
                type="number"
                min="0.01"
                step="0.01"
                value={amount}
                onChange={(event) => setAmount(event.target.value)}
                placeholder="0.00"
                required
              />
            </label>

            <button disabled={loading || accounts.length === 0}>
              Submit transaction
            </button>
          </form>
        </article>

        <article className="panel transactions-panel">
          <h2>Transaction history</h2>

          {transactions.length === 0 ? (
            <p>No transactions found.</p>
          ) : (
            <div className="transaction-list">
              {transactions.map((transaction) => {
                const isDeposit =
                  transaction.transactionType.toUpperCase() === "DEPOSIT";

                return (
                  <div className="transaction-row" key={transaction.id}>
                    <div>
                      <strong>{transaction.transactionType}</strong>
                      <p>
                        Account ending in{" "}
                        {transaction.accountId.slice(-4)}
                      </p>
                    </div>

                    <span
                      className={
                        isDeposit
                          ? "deposit-amount"
                          : "withdrawal-amount"
                      }
                    >
                      {isDeposit ? "+" : "-"}$
                      {Number(transaction.amount).toFixed(2)}
                    </span>
                  </div>
                );
              })}
            </div>
          )}
        </article>
      </section>
    </main>
  );
}

export default App;