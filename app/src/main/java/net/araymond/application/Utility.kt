package net.araymond.application

import android.content.Context
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object Utility {

    fun indexFromName(accountName: String): Int {
        for (i in Values.accounts.indices) {
            if (accountName.compareTo(Values.accounts[i].name) == 0) {
                return i
            }
        }
        return -1
    }

    fun readAccounts() {
        Values.accountsNames = ArrayList()
        for (account in Values.accounts) {
            Values.accountsNames.add(account.name)
        }
    }

    fun readCategories() {
        var transactions: ArrayList<Transaction>
        for (account in Values.accounts) {
            transactions = account.transactions
            for (transaction in transactions) {
                Values.categories.add(transaction.category)
            }
        }
        val duplicatesRemoved: HashSet<String> = HashSet(Values.categories)
        Values.categories = ArrayList(duplicatesRemoved)
    }

    fun readLedgerSaveData(context: Context): Boolean {
        return try {
            val inputLedgerStream = context.openFileInput("ledger")
            val objectInputLedgerStream = ObjectInputStream(inputLedgerStream)
            Values.accounts = objectInputLedgerStream.readObject() as ArrayList<Account>
            objectInputLedgerStream.close()
            inputLedgerStream.close()

            true
        } catch (exception: Exception) {
            false
        }
    }

    fun readCurrencySaveData(context: Context): Boolean {
        return try {
            val inputCurrencyStream = context.openFileInput("currency")
            val objectInputCurrencyStream = ObjectInputStream(inputCurrencyStream)
            Values.currency = objectInputCurrencyStream.readObject() as String
            objectInputCurrencyStream.close()
            inputCurrencyStream.close()

            true
        } catch (exception: Exception) {
            false
        }
    }

    fun writeSaveData(data: Any, file: String, context: Context): Boolean {
        return try {
            val outputStream = context.openFileOutput(file, Context.MODE_PRIVATE)
            val objectOutputStream = ObjectOutputStream(outputStream)
            objectOutputStream.writeObject(data)
            outputStream.flush()
            outputStream.close()
            objectOutputStream.close()

            true
        } catch (exception: Exception) {
            false
        }
    }

    fun writeLedgerData(context: Context): Boolean {
        return (writeSaveData(Values.accounts, "ledger", context))
    }

    fun writeCurrencyData(context: Context): Boolean {
        return (writeSaveData(Values.currency, "currency", context))
    }
}