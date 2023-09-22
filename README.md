# TransactionServer-Team10
https://www.youtube.com/watch?v=7RbJNK4t4Rg
Project Description

Please read the following paragraphs carefully and contact me with any questions that you have.
Data objects

Your transaction server will manage X number of data objects that can be thought of as banking accounts; each account is holding a certain amount of money. The number of accounts is configurable.

When you start your transaction server, each account is initialized to some configurable amount, e.g. $10. Accounts can be "over-drafted", resulting in a negative balance. For simplicity, we will assume that accounts contain only full dollar amounts. This will allow you to represent account balances by simple integers.

Operations on data objects
Account balances can be read and can be written - those are the two fundamental operations that can be applied to accounts. This allows, e.g. for reading an account balance and, depending on the value read, writing a different balance back. The combined effect of these two operations can be viewed as a withdrawal or a deposit, depending on whether the original balance was larger or smaller than the resulting one.
Because accounts can only contain full dollar amounts, write operations can also only write full dollar amounts.
Transactions

While conceptually your transaction server can support any number of read/write operations on its data objects in any order, we want to introduce yet another simplification as follows. All transactions hitting your server will only ever do the same set of operations that each and every transaction is comprised of: an arbitrarily chosen dollar amount is withdrawn from an arbitrarily chosen account and deposited onto another, arbitrarily chosen account. I ask you to always stay strictly at the level of elementary read/write operations, instead of  implementing higher level constructs like withdrawal and deposit.

As can be easily seen, this results in a "zero sum game", i.e. after any properly run number of such transactions has completed, the sum of all balances over all X accounts should be e.g. $10 times X.
From the above description it should be clear that all transactions look the same in what they do. However, a run of a certain transaction will be different from any other run of another transaction in that the dollar amounts to be transferred between accounts is chosen by chance, as are the source and destination accounts. Needless to say, transactions are uniquely identified by their transaction IDs. 

While you are allowed to make the assumptions about what all transactions uniformly look like, the only entity that is allowed to implement a "hard-coded" representation of activities that a transaction is comprised of is the client application itself. On the other hand, your transactional system, including the server proxy object on the client side, is absolutely unaware of the assumption of such a predefined sequence of activities, i.e. it can handle any activities in any order whatsoever, without any limitation or underlying assumption.

Clients will accept the connectivity information of the server as a configurable property. Convince yourself that all the client is doing with this information is to pass it on to the server proxy object, the latter handling the low-level communication with the server and advertising the transactional API to the client.
Your client(s) need to connect to your transaction server over a TCP/IP network.
