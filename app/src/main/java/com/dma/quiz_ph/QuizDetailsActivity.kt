package com.dma.quiz_ph

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dma.quiz_ph.Model.QnAResponse
import com.dma.quiz_ph.ViewModel.QnAViewModel
import com.dma.quiz_ph.databinding.ActivityQuizDetailsBinding
import com.dma.quiz_ph.utils.Constant
import com.dma.quiz_ph.utils.CustomLoading
import com.dma.quiz_ph.utils.DataStatus
import com.dma.quiz_ph.utils.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject


@AndroidEntryPoint
class QuizDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: QnAViewModel by viewModels()

    private var _binding: ActivityQuizDetailsBinding? = null
    private val binding get() = _binding!!

    private var totalQuestionToAnswer: Long = 0

    private var canAnswer = false
    private var currentQuestion = 0
    private var countDownTimer: CountDownTimer? = null

    private var allQuestionList: MutableList<QnAResponse.Question>? = null
    private var questionsToAnswer: MutableList<QnAResponse.Question> = ArrayList()
    private var optionList: MutableList<String> = ArrayList()

    private var customLoading: CustomLoading? = null

    var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityQuizDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customLoading = CustomLoading(this)

        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        getQuestionsAndAnswers()

        binding.apply {
            btnOptionOne.setOnClickListener(this@QuizDetailsActivity)
            btnOptionTwo.setOnClickListener(this@QuizDetailsActivity)
            btnOptionThree.setOnClickListener(this@QuizDetailsActivity)
            btnOptionFour.setOnClickListener(this@QuizDetailsActivity)

        }

    }

    private fun getQuestionsAndAnswers() {
        lifecycleScope.launch {
            viewModel.getQnA()
            viewModel.qnaResponse.observe(this@QuizDetailsActivity) {
                when (it.status) {
                    DataStatus.Status.LOADING -> {
                        customLoading!!.showLoading("Loading...")
                    }
                    DataStatus.Status.SUCCESS -> {
                        customLoading!!.dismissLoading()
                        Log.e("qna", it.data!!.questions.size.toString())
                        totalQuestionToAnswer = it.data.questions.size.toLong()
                        allQuestionList = it.data.questions as MutableList<QnAResponse.Question>
                        pickQuestion()

                        loadUI()
                    }

                    DataStatus.Status.ERROR -> {
                        customLoading!!.dismissLoading()
                        Toast.makeText(
                            this@QuizDetailsActivity,
                            "There is something wrong!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun loadUI() {
        binding.txtQstn.text = "Load First Question"
        enableOptions()
        loadQuestion(1)
    }

    @SuppressLint("SetTextI18n")
    private fun loadQuestion(questNum: Int) {
        binding.apply {
            txtQstnNo.text = "Question: $questNum/$totalQuestionToAnswer"
            txtQstnPoint.text = "${questionsToAnswer[questNum - 1].score} point"
            txtQstn.text = questionsToAnswer[questNum - 1].question

            /*optionList.add(questionsToAnswer[questNum - 1].answers.A)
            optionList.add(questionsToAnswer[questNum - 1].answers.B)
            optionList.add(questionsToAnswer[questNum - 1].answers.C)
            optionList.add(questionsToAnswer[questNum - 1].answers.D)
            val random : Int = getRandomInteger(optionList.size,0)

            btnOptionOne.text = optionList[getRandomInteger(optionList.size,0)]
            optionList.removeAt(getRandomInteger(optionList.size,0))
            btnOptionTwo.text = optionList[getRandomInteger(optionList.size,0)]
            optionList.removeAt(getRandomInteger(optionList.size,0))
            if (questionsToAnswer[questNum - 1].answers.C == null) {
                btnOptionThree.visibility = View.GONE
                btnOptionFour.visibility = View.GONE
            } else {
                btnOptionThree.visibility = View.VISIBLE
                btnOptionFour.visibility = View.VISIBLE
                btnOptionThree.text = optionList[getRandomInteger(optionList.size,0)]
                optionList.removeAt(getRandomInteger(optionList.size,0))
                btnOptionFour.text = optionList[getRandomInteger(optionList.size,0)]
                optionList.removeAt(getRandomInteger(optionList.size,0))
            }*/

            btnOptionOne.text = questionsToAnswer[questNum - 1].answers.A
            btnOptionTwo.text = questionsToAnswer[questNum - 1].answers.B
            if (questionsToAnswer[questNum - 1].answers.C == null) {
                btnOptionThree.visibility = View.GONE
                btnOptionFour.visibility = View.GONE
            } else {
                btnOptionThree.visibility = View.VISIBLE
                btnOptionFour.visibility = View.VISIBLE
                btnOptionThree.text = questionsToAnswer[questNum - 1].answers.C
                btnOptionFour.text = questionsToAnswer[questNum - 1].answers.D
            }

            if (questionsToAnswer[questNum-1].questionImageUrl != null){
                Glide
                    .with(this@QuizDetailsActivity)
                    .load(questionsToAnswer[questNum-1].questionImageUrl)
                    .centerCrop()
                    .into(imgQstn);
            } else{
                imgQstn.setBackgroundResource(R.drawable.ph_logo)
            }

        }
        canAnswer = true
        currentQuestion = questNum
        startTimer()
    }


    private fun startTimer() {
        val timeToAnswer: Long = 10
        binding.apply {
            quizTime.text = timeToAnswer.toString()
            quizQuestionProgressbar.visibility = View.VISIBLE
            countDownTimer = object : CountDownTimer(timeToAnswer * 1000, 10) {
                override fun onTick(l: Long) {
                    quizTime.text = (l / 1000).toString() + ""
                    val percent = l / (timeToAnswer * 10)
                    quizQuestionProgressbar.progress = percent.toInt()
                }

                @SuppressLint("NewApi")
                override fun onFinish() {
                    canAnswer = false
                    showNext()
                }
            }
            (countDownTimer as CountDownTimer).start()
        }
    }

    private fun startTimerForNextQuestion() {
        val timeToAnswer: Long = 2
        countDownTimer = object : CountDownTimer(timeToAnswer * 1000, 10) {
            override fun onTick(l: Long) {
            }
            @SuppressLint("NewApi")
            override fun onFinish() {
                showNext()

            }
        }
        (countDownTimer as CountDownTimer).start()

    }

    private fun enableOptions() {
        binding.apply {
            btnOptionOne.visibility = View.VISIBLE
            btnOptionTwo.visibility = View.VISIBLE
            btnOptionThree.visibility = View.VISIBLE
            btnOptionFour.visibility = View.VISIBLE
            btnOptionOne.isEnabled = true
            btnOptionTwo.isEnabled = true
            btnOptionThree.isEnabled = true
            btnOptionFour.isEnabled = true
        }
    }

    private fun pickQuestion() {
        for (i in 0 until totalQuestionToAnswer) {
            val randomNumber: Int = getRandomInteger(allQuestionList!!.size, 0)
            questionsToAnswer.add(allQuestionList!![randomNumber])
            allQuestionList!!.removeAt(randomNumber)
        }
    }


    private fun getRandomInteger(maximum: Int, minimum: Int): Int {
        return (Math.random() * (maximum - minimum)).toInt() + minimum
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun resetOptions() {
        binding.apply {
            btnOptionOne.background = resources.getDrawable(R.drawable.button_bg, null)
            btnOptionTwo.background = resources.getDrawable(R.drawable.button_bg, null)
            btnOptionThree.background = resources.getDrawable(R.drawable.button_bg, null)
            btnOptionFour.background = resources.getDrawable(R.drawable.button_bg, null)
        }
    }

    private fun verifyAnswer(selectedAnswerButton: AppCompatButton, s: String) {
        if (canAnswer) {

            if (questionsToAnswer[currentQuestion - 1].correctAnswer == s) {
                score += questionsToAnswer[currentQuestion - 1].score
                binding.txtScore.text = "Score: $score"
                selectedAnswerButton.background =
                    resources.getDrawable(R.drawable.correct_ans_button, null)

            } else {
                selectedAnswerButton.background = resources.getDrawable(R.drawable.wrong_ans_button_bg, null)
                var correctButton : String = questionsToAnswer[currentQuestion - 1].correctAnswer
                when (correctButton){
                    "A" -> binding.btnOptionOne.background =resources.getDrawable(R.drawable.correct_ans_button, null)
                    "B" -> binding.btnOptionTwo.background =resources.getDrawable(R.drawable.correct_ans_button, null)
                    "C" -> binding.btnOptionThree.background =resources.getDrawable(R.drawable.correct_ans_button, null)
                    "D" -> binding.btnOptionFour.background =resources.getDrawable(R.drawable.correct_ans_button, null)
                }

            }
            canAnswer = false
            countDownTimer!!.cancel()
            startTimerForNextQuestion()
        }
    }

    private fun showNext() {
        if (currentQuestion.toLong() == totalQuestionToAnswer) {
            PreferenceHelper.insertData(this@QuizDetailsActivity,Constant.HIGHEST_SCORE,score.toString())
            startActivity(Intent(this@QuizDetailsActivity, MainActivity::class.java))
        } else{
            currentQuestion++
            loadQuestion(currentQuestion)
            resetOptions()
        }

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnOptionOne -> verifyAnswer(binding.btnOptionOne, "A")
            R.id.btnOptionTwo -> verifyAnswer(binding.btnOptionTwo, "B")
            R.id.btnOptionThree -> verifyAnswer(binding.btnOptionThree, "C")
            R.id.btnOptionFour -> verifyAnswer(binding.btnOptionFour, "D")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}