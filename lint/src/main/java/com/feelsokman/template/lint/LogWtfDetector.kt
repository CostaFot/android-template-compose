package com.feelsokman.template.lint

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

class LogWtfDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String>? = listOf()

    override fun visitMethodCall(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
        val evaluator = context.evaluator
        if (evaluator.isMemberInClass(method, "androidx.core.view.WindowCompat")) {
            val quickFixData = LintFix
                .create()
                .name("use Log.e()")
                .replace()
                .text(method.name)
                .with("e")
                .robot(true)
                .independent(true)
                .build()

            context.report(
                ISSUE, node, context.getLocation(node),
                "hello",
                quickFixData
            )
        }
    }

    companion object {
        @JvmField
        val ISSUE: Issue = Issue.create(
            id = "Logwtf",
            briefDescription = "Logwtf",
            explanation = "Test lint Log",
            category = Category.CUSTOM_LINT_CHECKS,
            priority = 7,
            severity = Severity.FATAL,
            implementation = Implementation(
                LogWtfDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

}
