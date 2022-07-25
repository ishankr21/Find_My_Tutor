package com.ishankumar.findmytutor.features.transactionHistory

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ishankumar.findmytutor.dataClasses.TransactionInfo
import com.ishankumar.findmytutor.databinding.*
import java.text.SimpleDateFormat

class TransactionHistoryAdapter(
    var transactionList:ArrayList<TransactionInfo>,
    val context: Context,
    private val userId:String
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class CreditViewModel(val binding:CreditFeesItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bindItems(transactionInfo: TransactionInfo) {

            binding.txtPaidBy.text="Paid by : "+transactionInfo.studentName
            binding.txtTransactionFeesForMonth.text="Payment for the month : "+transactionInfo.paymentForMonth
            binding.txtTransactionId.text="Transaction Id : "+transactionInfo.transactionId
            binding.txtTransactionCreditAmount.text="Amount Credited : "+transactionInfo.amount
            val date = transactionInfo.completedOn!!.toDate()

            val simpleDateFormat = SimpleDateFormat("dd MMM yyyy")
            binding.txtTransactionDate.text="Completed On : "+simpleDateFormat.format(date)



        }
    }
    inner class DebitViewModel(val binding: DebitFeesItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
            fun bindItems(transactionInfo: TransactionInfo) {

                binding.txtPaidTo.text="Paid to : "+transactionInfo.tutorName
                binding.txtTransactionFeesForMonth.text="Payment for the month : "+transactionInfo.paymentForMonth
                binding.txtTransactionId.text="Transaction Id : "+transactionInfo.transactionId
                binding.txtTransactionDebitedAmount.text="Amount Debited : "+transactionInfo.amount
                val date = transactionInfo.completedOn!!.toDate()

                val simpleDateFormat = SimpleDateFormat("dd MMM yyyy")
                binding.txtTransactionDate.text="Completed On : "+simpleDateFormat.format(date)


            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType==1) {
            CreditViewModel(
                CreditFeesItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
            )
        } else {
            DebitViewModel(
                DebitFeesItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val transactionInfo=transactionList[position]
        if(transactionInfo.studentId==userId)
        {
            val viewHolder=holder as DebitViewModel
            viewHolder.bindItems(transactionInfo)
        }
        else
        {
            val viewHolder=holder as CreditViewModel
            viewHolder.bindItems(transactionInfo)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val transactionInfo=transactionList[position]
        return if(transactionInfo.studentId==userId) {
            2
        } else {
            1
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}