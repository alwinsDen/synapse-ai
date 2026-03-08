
import androidx.compose.runtime.Composable
import com.alwinsden.dino.botInterface.components.ModelDefinitions
import com.alwinsden.dino.botInterface.components.ModelSelectionParamDetails
import com.alwinsden.dino.botInterface.components.ModelSelectionRadioMenu
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

private class ModelSelectPreviewProvider : PreviewParameterProvider<ModelSelectionParamDetails> {
    override val values = sequenceOf(
        ModelSelectionParamDetails(
            name = "Google Gemini",
            description = "Advanced model from Google",
            incomingType = ModelDefinitions.LOCL_V1.name,
        ),
        ModelSelectionParamDetails(
            name = "Claude Opus",
            description = "Model from Anthropic",
            incomingType = ModelDefinitions.CLAUDE.name,
        ),
        ModelSelectionParamDetails(
            name = "Locl V1",
            description = "V1 model from Locl",
            incomingType = ModelDefinitions.LOCL_V1.name,
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun ModelSelectionRadioMenuPreview(
    @PreviewParameter(ModelSelectPreviewProvider::class) incomingClass: ModelSelectionParamDetails
) {
    ModelSelectionRadioMenu(
        onClick = { tst ->

        },
        state = incomingClass,
        currentSelection = ModelDefinitions.CLAUDE.name
    )
}