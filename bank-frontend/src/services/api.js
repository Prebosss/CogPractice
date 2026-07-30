const API_BASE_URL = "/api/v1";

async function request(endpoint, options = {}) {
  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    headers: {
      "Content-Type": "application/json",
      ...options.headers,
    },
    ...options,
  });

  if (!response.ok) {
    let message = `Request failed with status ${response.status}`;

    try {
      const errorBody = await response.json();

      if (errorBody.message) {
        message = errorBody.message;
      } else if (errorBody.error) {
        message = errorBody.error;
      }
    } catch {
      // The response may not contain JSON.
    }

    throw new Error(message);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

/* Users */

export function loginUser(username, password) {
  return request("/users/login", {
    method: "POST",
    body: JSON.stringify({
      username,
      password,
    }),
  });
}

export function createUser(username, password) {
  return request("/users", {
    method: "POST",
    body: JSON.stringify({
      username,
      password,
    }),
  });
}

/* Accounts */

export function getAccounts() {
  return request("/accounts");
}

export function createAccount(userId, accountType) {
  return request("/accounts", {
    method: "POST",
    body: JSON.stringify({
      userId,
      accountType,
    }),
  });
}

/* Transactions */

export function getTransactions() {
  return request("/transactions");
}

export function createTransaction(accountId, amount, transactionType) {
  return request("/transactions", {
    method: "POST",
    body: JSON.stringify({
      accountId,
      amount: Number(amount),
      transactionType,
    }),
  });
}