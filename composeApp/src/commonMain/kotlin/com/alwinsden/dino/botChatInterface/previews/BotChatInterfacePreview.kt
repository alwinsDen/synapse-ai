package com.alwinsden.dino.botChatInterface.previews

import androidx.compose.runtime.Composable
import com.alwinsden.dino.botChatInterface.BotChatInterface
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
private fun BotChatInterfacePreview() {
    BotChatInterface()
}

//test objects
object chat_samples_BotChatInterface {
    var user_message = "Hey, how are you doing? I had a great day today. It was awesome!"
    var model_response =
        "That’s fantastic to hear! There is nothing quite like that \"on top of the world\" feeling after a truly killer day. I'm doing great myself, especially now that I'm catching some of that positive energy you're putting out.\n" +
                "\n" +
                "What made it so awesome? Did you hit a major milestone, or was it just one of those days where everything finally clicked into place?\n" +
                "\n" +
                "I'd love to hear the highlights if you're up for sharing!"
    var chat_title = "Checking on me how I am...."
}