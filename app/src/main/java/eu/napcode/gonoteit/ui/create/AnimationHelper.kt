package eu.napcode.gonoteit.ui.create

import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionManager
import eu.napcode.gonoteit.R


fun animateToDisplayDate(constraintLayout: ConstraintLayout) {
    TransitionManager.beginDelayedTransition(constraintLayout)

    var sourceConstraintSet = ConstraintSet()
    sourceConstraintSet.clone(constraintLayout)

    sourceConstraintSet.setVisibility(R.id.dateTextView, ConstraintSet.VISIBLE)

    sourceConstraintSet.connect(
            R.id.createNoteButton, ConstraintSet.START,
            R.id.addImageButton, ConstraintSet.END
    )
    sourceConstraintSet.connect(
            R.id.createNoteButton, ConstraintSet.LEFT,
            R.id.addImageButton, ConstraintSet.RIGHT
    )
    sourceConstraintSet.connect(
            R.id.createNoteButton, ConstraintSet.TOP,
            R.id.addDateButton, ConstraintSet.BOTTOM
    )

    sourceConstraintSet.connect(
            R.id.addDateButton, ConstraintSet.BOTTOM,
            R.id.dateTextView, ConstraintSet.BOTTOM
    )
    sourceConstraintSet.connect(
            R.id.addDateButton, ConstraintSet.TOP,
            R.id.dateTextView, ConstraintSet.TOP
    )
    sourceConstraintSet.connect(
            R.id.addDateButton, ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID, ConstraintSet.LEFT
    )

    sourceConstraintSet.applyTo(constraintLayout)
}

fun animateToHideDate(constraintLayout: ConstraintLayout) {
    TransitionManager.beginDelayedTransition(constraintLayout)

    var sourceConstraintSet = ConstraintSet()
    sourceConstraintSet.clone(constraintLayout)

    sourceConstraintSet.setVisibility(R.id.dateTextView, ConstraintSet.GONE)

    sourceConstraintSet.connect(
            R.id.createNoteButton, ConstraintSet.START,
            R.id.addDateButton, ConstraintSet.END
    )
    sourceConstraintSet.connect(
            R.id.createNoteButton, ConstraintSet.LEFT,
            R.id.addDateButton, ConstraintSet.RIGHT
    )
    sourceConstraintSet.connect(
            R.id.createNoteButton, ConstraintSet.TOP,
            R.id.contentBarrier, ConstraintSet.BOTTOM
    )

    sourceConstraintSet.connect(
            R.id.addDateButton, ConstraintSet.BOTTOM,
            R.id.createNoteButton, ConstraintSet.BOTTOM
    )
    sourceConstraintSet.connect(
            R.id.addDateButton, ConstraintSet.TOP,
            R.id.createNoteButton, ConstraintSet.TOP
    )
    sourceConstraintSet.connect(
            R.id.addDateButton, ConstraintSet.LEFT,
            R.id.addImageButton, ConstraintSet.RIGHT
    )

    sourceConstraintSet.applyTo(constraintLayout)
}
