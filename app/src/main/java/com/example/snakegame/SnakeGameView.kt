package com.example.snakegame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random

class SnakeGameView(context: Context) : View(context) {

    private val paint = Paint()
    private val snake = mutableListOf<Point>()
    private var food = Point()
    private var direction = Direction.RIGHT
    private var nextDirection = Direction.RIGHT
    private var score = 0
    private var gameOver = false
    private var cellSize = 0f
    private var cols = 20
    private var rows = 30

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (!gameOver) {
                update()
                invalidate()
                handler.postDelayed(this, 150)
            }
        }
    }

    enum class Direction { UP, DOWN, LEFT, RIGHT }

    init {
        resetGame()
        handler.post(updateRunnable)
    }

    private fun resetGame() {
        snake.clear()
        snake.add(Point(5, 10))
        snake.add(Point(4, 10))
        snake.add(Point(3, 10))
        direction = Direction.RIGHT
        nextDirection = Direction.RIGHT
        score = 0
        gameOver = false
        spawnFood()
    }

    private fun spawnFood() {
        do {
            food = Point(Random.nextInt(cols), Random.nextInt(rows))
        } while (snake.any { it.x == food.x && it.y == food.y })
    }

    private fun update() {
        direction = nextDirection
        val head = snake.first()
        val newHead = when (direction) {
            Direction.UP -> Point(head.x, head.y - 1)
            Direction.DOWN -> Point(head.x, head.y + 1)
            Direction.LEFT -> Point(head.x - 1, head.y)
            Direction.RIGHT -> Point(head.x + 1, head.y)
        }

        // Wall collision
        if (newHead.x < 0 || newHead.x >= cols || newHead.y < 0 || newHead.y >= rows) {
            gameOver = true
            return
        }

        // Self collision
        if (snake.any { it.x == newHead.x && it.y == newHead.y }) {
            gameOver = true
            return
        }

        snake.add(0, newHead)

        if (newHead.x == food.x && newHead.y == food.y) {
            score += 10
            spawnFood()
        } else {
            snake.removeAt(snake.lastIndex)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cellSize = minOf(w / cols.toFloat(), h / rows.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Background
        canvas.drawColor(Color.rgb(20, 30, 20))

        // Grid (optional subtle)
        paint.color = Color.rgb(30, 45, 30)
        paint.strokeWidth = 1f
        for (i in 0..cols) {
            canvas.drawLine(i * cellSize, 0f, i * cellSize, rows * cellSize, paint)
        }
        for (i in 0..rows) {
            canvas.drawLine(0f, i * cellSize, cols * cellSize, i * cellSize, paint)
        }

        // Snake
        paint.style = Paint.Style.FILL
        snake.forEachIndexed { index, point ->
            paint.color = if (index == 0) Color.rgb(50, 205, 50) else Color.rgb(34, 139, 34)
            canvas.drawRect(
                point.x * cellSize + 1,
                point.y * cellSize + 1,
                (point.x + 1) * cellSize - 1,
                (point.y + 1) * cellSize - 1,
                paint
            )
        }

        // Food
        paint.color = Color.RED
        canvas.drawCircle(
            food.x * cellSize + cellSize / 2,
            food.y * cellSize + cellSize / 2,
            cellSize / 2.5f,
            paint
        )

        // Score
        paint.color = Color.WHITE
        paint.textSize = 50f
        canvas.drawText("Счёт: $score", 30f, 60f, paint)

        if (gameOver) {
            paint.color = Color.WHITE
            paint.textSize = 70f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("Игра окончена", width / 2f, height / 2f - 40, paint)
            paint.textSize = 40f
            canvas.drawText("Нажмите, чтобы начать заново", width / 2f, height / 2f + 40, paint)
            paint.textAlign = Paint.Align.LEFT
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (gameOver) {
                resetGame()
                handler.post(updateRunnable)
                return true
            }

            val x = event.x
            val y = event.y
            val head = snake.first()
            val headX = head.x * cellSize + cellSize / 2
            val headY = head.y * cellSize + cellSize / 2

            val dx = x - headX
            val dy = y - headY

            if (kotlin.math.abs(dx) > kotlin.math.abs(dy)) {
                // Horizontal
                if (dx > 0 && direction != Direction.LEFT) nextDirection = Direction.RIGHT
                else if (dx < 0 && direction != Direction.RIGHT) nextDirection = Direction.LEFT
            } else {
                // Vertical
                if (dy > 0 && direction != Direction.UP) nextDirection = Direction.DOWN
                else if (dy < 0 && direction != Direction.DOWN) nextDirection = Direction.UP
            }
        }
        return true
    }
}
