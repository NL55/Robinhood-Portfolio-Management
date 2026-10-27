
import axios from 'axios';

// Trade Execution Service API client
// Nginx forwards /api/trades requests to the
// Trade Execution Service Docker container on port 8082.

const tradeClient = axios.create({
  baseURL: '',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Execute BUY or SELL trade
export const executeTrade = (tradeData) =>
  tradeClient.post('/api/trades', tradeData);

// Get trade details by trade ID
export const getTradeById = (id) =>
  tradeClient.get(`/api/trades/${id}`);

// Get all trades for a specific user
export const getTradesByUserId = (userId) =>
  tradeClient.get(`/api/trades/user/${userId}`);

// Get trades by stock symbol
export const getTradesByStockSymbol = (stockSymbol) =>
  tradeClient.get(`/api/trades/stock/${stockSymbol}`);
