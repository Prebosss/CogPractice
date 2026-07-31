import { useEffect, useState } from "react";
import {
  createAccount,
  createTransaction,
  getAccounts,
  getTransactions,
  loginUser,
  registerUser,
} from "./services/api";
import "./App.css";

function App() {
  const [currentUser, setCurrentUser] = useState(() => {
    const savedUser = localStorage.getItem("bankUser");
    const savedToken = localStorage.getItem("bankToken");

    if (!savedUser || !savedToken) {
      return null;
    }

    return JSON.parse(savedUser);
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

  const totalBalance = accounts.reduce(
    (total, account) => total + Number(account.balance),
    0,
  );

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

      const authResponse =
        authMode === "login"
          ? await loginUser(username, password)
          : await registerUser(username, password);

      localStorage.setItem(
        "bankToken",
        authResponse.token,
      );

      localStorage.setItem(
        "bankUser",
        JSON.stringify(authResponse.user),
      );

      setCurrentUser(authResponse.user);
      setUsername("");
      setPassword("");
    } catch (err) {
      setError(err.message);
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

      setMessage(
        `${accountType === "SAVINGS" ? "Savings" : "Checking"} account created.`,
      );
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
        transactionType === "DEPOSIT"
          ? "Deposit completed."
          : "Withdrawal completed.",
      );
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  function handleLogout() {
    localStorage.removeItem("bankToken");
    localStorage.removeItem("bankUser");
    setCurrentUser(null);
    setAuthMode("login");

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
          <h1>Electro Bank</h1>

          <p>
            {authMode === "login"
              ? "Welcome back. Sign in to the network to view your money."
              : "Create your profile and join the network."}
          </p>

          <form onSubmit={handleAuthentication}>
            <label>
              Username
              <input
                type="text"
                value={username}
                onChange={(event) => setUsername(event.target.value)}
                placeholder="Enter your username"
                autoComplete="username"
                required
              />
            </label>

            <label>
              Password
              <input
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                placeholder="Enter your password"
                autoComplete={
                  authMode === "login"
                    ? "current-password"
                    : "new-password"
                }
                required
              />
            </label>

            {error && <p className="error-message">{error}</p>}

            <button type="submit" disabled={loading}>
              {loading
                ? "Please wait..."
                : authMode === "login"
                  ? "Log in"
                  : "Create account"}
            </button>
          </form>

          <button
            type="button"
            className="text-button"
            onClick={() => {
              setAuthMode(
                authMode === "login" ? "register" : "login",
              );
              setError("");
              setMessage("");
            }}
          >
            {authMode === "login"
              ? "New here? Create an account"
              : "Already have an account? Log in"}
          </button>
        </section>
      </main>
    );
  }

  return (
    <main className="dashboard">
      <header className="dashboard-header">
        <div className="brand">
          <div className="brand-logo">★</div>

          <div>
            <h1>Electro Bank</h1>
            <p>Digital Banking // always online</p>
          </div>
        </div>

        <div className="header-user">
          <div className="user-avatar">
            {currentUser.username.charAt(0).toUpperCase()}
          </div>

          <div className="user-details">
            <span>Welcome back</span>
            <strong>{currentUser.username}</strong>
          </div>

          <button
            type="button"
            className="logout-button"
            onClick={handleLogout}
          >
            Log out
          </button>
        </div>
      </header>

      <section className="balance-hero">
        <div className="hero-decoration hero-decoration-one"></div>
        <div className="hero-decoration hero-decoration-two"></div>

        <div className="balance-content">
          <span className="eyebrow">Total balance</span>

          <h2>
            $
            {totalBalance.toLocaleString("en-US", {
              minimumFractionDigits: 2,
              maximumFractionDigits: 2,
            })}
          </h2>

          <p>
            Across {accounts.length}{" "}
            {accounts.length === 1 ? "account" : "accounts"}
          </p>
        </div>

        <div className="balance-emoji">☆</div>
      </section>

      {error && <p className="error-message alert">{error}</p>}

      {message && (
        <p className="success-message alert">{message}</p>
      )}

      <section className="dashboard-grid">
        <article className="panel accounts-panel">
          <div className="panel-heading">
            <div>
              <span className="section-icon">💳</span>
              <h2>Your accounts</h2>
            </div>

            <span className="account-count">{accounts.length}</span>
          </div>

          {loading && accounts.length === 0 ? (
            <p>Loading your accounts...</p>
          ) : accounts.length === 0 ? (
            <p className="panel-description">
              You do not have any accounts yet. Open one below to
              get started.
            </p>
          ) : (
            <div className="account-list">
              {accounts.map((account) => (
                <div className="account-card" key={account.id}>
                  <div className="account-card-left">
                    <div
                      className={`account-icon ${account.accountType === "SAVINGS"
                        ? "savings-icon"
                        : "checking-icon"
                        }`}
                    >
                      {account.accountType === "SAVINGS"
                        ? "🐷"
                        : "💳"}
                    </div>

                    <div>
                      <span className="account-type">
                        {account.accountType === "SAVINGS"
                          ? "Savings account"
                          : "Checking account"}
                      </span>

                      <p className="account-id">
                        •••• {account.id.slice(-4)}
                      </p>
                    </div>
                  </div>

                  <div className="account-balance">
                    <span>Available</span>

                    <strong>
                      $
                      {Number(account.balance).toLocaleString(
                        "en-US",
                        {
                          minimumFractionDigits: 2,
                          maximumFractionDigits: 2,
                        },
                      )}
                    </strong>
                  </div>
                </div>
              ))}
            </div>
          )}
        </article>

        <article className="panel">
          <div className="panel-heading">
            <div>
              <span className="section-icon">✨</span>
              <h2>Open a new account</h2>
            </div>
          </div>

          <p className="panel-description">
            Pick an account type and start saving.
          </p>

          <form onSubmit={handleCreateAccount}>
            <label>
              Account type
              <select
                value={accountType}
                onChange={(event) =>
                  setAccountType(event.target.value)
                }
              >
                <option value="CHECKING">Checking</option>
                <option value="SAVINGS">Savings</option>
              </select>
            </label>

            <button type="submit" disabled={loading}>
              {loading ? "Creating..." : "Create account"}
            </button>
          </form>
        </article>

        <article className="panel">
          <div className="panel-heading">
            <div>
              <span className="section-icon">↗️</span>
              <h2>Move money</h2>
            </div>
          </div>

          <p className="panel-description">
            Deposit money or make a withdrawal.
          </p>

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
              Transaction type
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

            <button
              type="submit"
              disabled={loading || accounts.length === 0}
            >
              {loading ? "Processing..." : "Submit transaction"}
            </button>
          </form>
        </article>

        <article className="panel transactions-panel">
          <div className="panel-heading">
            <div>
              <span className="section-icon">🧾</span>
              <h2>Recent activity</h2>
            </div>
          </div>

          {transactions.length === 0 ? (
            <p className="panel-description">
              No transactions yet. Your activity will appear here.
            </p>
          ) : (
            <div className="transaction-list">
              {transactions.map((transaction) => {
                const normalizedType =
                  transaction.transactionType.toUpperCase();

                const isDeposit =
                  normalizedType === "DEPOSIT";

                return (
                  <div
                    className="transaction-row"
                    key={transaction.id}
                  >
                    <div>
                      <strong>
                        {isDeposit ? "Deposit" : "Withdrawal"}
                      </strong>

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