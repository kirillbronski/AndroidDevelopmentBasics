package com.kbcoding.core.model.tasks

import android.os.Handler
import android.os.Looper
import com.kbcoding.core.model.ErrorResult
import com.kbcoding.core.model.FinalResult
import com.kbcoding.core.model.SuccessResult
import com.kbcoding.core.model.tasks.dispatchers.Dispatcher
import com.kbcoding.core.model.tasks.factories.TaskBody
import com.kbcoding.core.model.tasks.factories.TasksFactory
import java.lang.Exception

private val handler = Handler(Looper.getMainLooper())

