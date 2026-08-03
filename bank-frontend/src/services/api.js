const API_BASE_URL =
    `${import.meta.env.VITE_API_URL}/api/v1`;
function getToken() {
    return localStorage.getItem("bankToken");
}

async function request(endpoint, options = {}) {
    const token = localStorage.getItem("bankToken");

    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...(token
                ? { Authorization: `Bearer ${token}` }
                : {}),
            ...options.headers,
        },
    });

    if (!response.ok) {
        let message = `Request failed with status ${response.status}`;

        try {
            const errorBody = await response.json();

            message =
                errorBody.detail ||
                errorBody.message ||
                errorBody.error ||
                message;
        } catch {
            // No JSON response body.
        }

        if (response.status === 401) {
            const isLoginRequest =
                endpoint === "/auth/login";

            if (isLoginRequest) {
                throw new Error(
                    "Incorrect username or password."
                );
            }

            localStorage.removeItem("bankToken");
            localStorage.removeItem("bankUser");

            throw new Error(
                "Session expired. Please log in again."
            );
        }

        throw new Error(message);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

export function loginUser(username, password) {
    return request("/auth/login", {
        method: "POST",
        body: JSON.stringify({
            username,
            password,
        }),
    });
}

export function registerUser(username, password) {
    return request("/auth/register", {
        method: "POST",
        body: JSON.stringify({
            username,
            password,
        }),
    });
}

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

export function getTransactions() {
    return request("/transactions");
}

export function createTransaction(
    accountId,
    amount,
    transactionType,
) {
    return request("/transactions", {
        method: "POST",
        body: JSON.stringify({
            accountId,
            amount: Number(amount),
            transactionType,
        }),
    });
}