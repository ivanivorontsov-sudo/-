package com.ivan.calculator

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : Activity() {
    private lateinit var display: TextView
    private var value = "0"
    private var stored: BigDecimal? = null
    private var operation: String? = null
    private var fresh = true

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); buildUi() }

    private fun buildUi() {
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(16,24,16,16); setBackgroundColor(Color.WHITE) }
        display = TextView(this).apply { text="0"; textSize=48f; gravity=Gravity.BOTTOM or Gravity.END; setTextColor(Color.BLACK); setPadding(8,8,8,20) }
        root.addView(display, LinearLayout.LayoutParams(-1,0,1f))
        val grid = GridLayout(this).apply { columnCount=4; rowCount=5 }
        arrayOf("AC","⌫","%","÷","7","8","9","×","4","5","6","−","1","2","3","+","±","0",".","=").forEach { key ->
            val b=Button(this).apply { text=key; textSize=22f; isAllCaps=false; setOnClickListener { press(key) } }
            grid.addView(b, GridLayout.LayoutParams().apply { width=0; height=0; columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1f); rowSpec=GridLayout.spec(GridLayout.UNDEFINED,1f); setMargins(3,3,3,3) })
        }
        root.addView(grid, LinearLayout.LayoutParams(-1,0,1.15f)); setContentView(root)
    }

    private fun press(key:String) {
        when(key) {
            "AC" -> { value="0"; stored=null; operation=null; fresh=true }
            "⌫" -> if(!fresh) value=value.dropLast(1).ifEmpty{"0"}
            "." -> if(!value.contains('.')) value += "."
            "±" -> if(value!="0" && value!="Ошибка") value=if(value.startsWith("-")) value.drop(1) else "-$value"
            "%" -> value=bd(value).divide(BigDecimal(100),12,RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
            "+","−","×","÷" -> chooseOp(key)
            "=" -> calculate()
            else -> if(key.all(Char::isDigit)) { if(fresh || value=="0" || value=="Ошибка") value=key else value+=key; fresh=false }
        }
        display.text=value
    }
    private fun chooseOp(op:String) { if(stored!=null && !fresh) calculate(); stored=bd(value); operation=op; fresh=true }
    private fun calculate() {
        val a=stored ?: return; val b=bd(value)
        val r=when(operation) { "+"->a.add(b); "−"->a.subtract(b); "×"->a.multiply(b); "÷"->if(b.compareTo(BigDecimal.ZERO)==0)null else a.divide(b,12,RoundingMode.HALF_UP); else->null }
        value=if(r==null) "Ошибка" else r.stripTrailingZeros().toPlainString(); stored=null; operation=null; fresh=true
    }
    private fun bd(s:String)=s.toBigDecimalOrNull() ?: BigDecimal.ZERO
}
