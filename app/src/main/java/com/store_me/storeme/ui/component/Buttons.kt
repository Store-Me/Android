package com.store_me.storeme.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.ui.main.MainActivity
import com.store_me.storeme.ui.theme.EditButtonColor
import com.store_me.storeme.ui.theme.SelectedCheckBoxColor
import com.store_me.storeme.ui.theme.TextClearIconColor
import com.store_me.storeme.ui.theme.ToggleButtonBorderColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.NavigationUtils
import com.store_me.storeme.utils.SizeUtils


/*
 * Button Composable 관리 파일
 */

/**
 * 기본 버튼
 */
@Composable
fun DefaultButton(
    buttonText: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        contentColor = Color.White,
        containerColor = Color.Black
    ),
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = colors,
        enabled = enabled
    ) {
        Text(
            text = buttonText,
            style = storeMeTextStyle(FontWeight.ExtraBold, 3),
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
    }
}

@Composable
fun DefaultButton(
    buttonText: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        contentColor = Color.White,
        containerColor = Color.Black
    ),
    leftIconResource: Int? = null,
    rightIconResource: Int? = null,
    leftIconTint: Color = Color.Unspecified,
    rightIconTint: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    val density = LocalDensity.current

    val iconSize = SizeUtils.textSizeToDp(density, 3)

    Button(
        onClick = { onClick() },
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = colors,
        enabled = enabled
    ) {
        if(leftIconResource != null) {
            Icon(
                painter = painterResource(id = leftIconResource),
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize),
                tint = leftIconTint
            )

            Spacer(modifier = Modifier.width(4.dp))
        }

        Text(
            text = buttonText,
            style = storeMeTextStyle(FontWeight.ExtraBold, 3),
            modifier = Modifier
                .padding(vertical = 8.dp)
        )

        if(rightIconResource != null) {
            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                painter = painterResource(id = rightIconResource),
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize),
                tint = rightIconTint
            )
        }
    }
}

/**
 * Stroke 를 가지는 Button
 */
@Composable
fun StrokeButton(text: String, onClick: () -> Unit){
    Button(
        modifier = Modifier
            .wrapContentWidth()
            .height(26.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(horizontal = 10.dp),
        onClick = onClick
    ) {
        Text(
            text = text,
            style = storeMeTextStyle(FontWeight.ExtraBold, 0)
        )
    }
}

/**
 * 닫기 버튼
 */
@Composable
fun DeleteButton(onClick: () -> Unit) {
    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
        contentDescription = "닫기",
        modifier = Modifier
            .size(20.dp)
            .clickable(
                onClick = { onClick() },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false)
            )
            .padding(2.dp)
    )
}

/**
 * Small size Button
 * @param text Button Text
 * @param modifier Modifier
 * @param containerColor Container Color
 * @param contentColor Content Color
 */
@Composable
fun SmallButton(
    text: String,
    modifier: Modifier = Modifier
        .height(40.dp)
        .fillMaxWidth(),
    containerColor: Color = EditButtonColor,
    contentColor: Color = Color.Black,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onClick,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text, style = storeMeTextStyle(FontWeight.ExtraBold, 0))
    }
}

/**
 * Large size Button
 * @param text Button Text
 * @param iconResource Icon Resource id
 * @param containerColor Container Color
 * @param contentColor Content Color
 * @param enabled Button Enabled
 */
@Composable
fun LargeButton(
    text: String,
    iconResource: Int? = null,
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val density = LocalDensity.current

    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        enabled = enabled,
        onClick = onClick,
    ) {
        iconResource?.let {
            Icon(
                painter = painterResource(id = iconResource),
                contentDescription = "아이콘",
                modifier = Modifier
                    .size(SizeUtils.textSizeToDp(density, 2))
                    .clip(CircleShape),
                tint = contentColor
            )

            Spacer(modifier = Modifier.width(5.dp))
        }

        Text(text = text, style = storeMeTextStyle(FontWeight.ExtraBold, 2))
    }
}

/*   ICON BUTTONS   */
@Composable
fun SearchButton(onClick: () -> Unit) {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
        contentDescription = "검색",
        modifier = Modifier
            .size(24.dp)
            .clickable(
                onClick = { onClick() },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false)
            ),
        tint = TextClearIconColor
    )
}

@Composable
fun NotificationIcon(navController: NavController) {
    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.ic_notification_off),
        contentDescription = "알림",
        modifier = Modifier
            .size(26.dp)
            .clickable(
                onClick = {
                    NavigationUtils().navigateCustomerNav(
                        navController,
                        MainActivity.CustomerNavItem.NOTIFICATION
                    )
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false)
            )
    )
}

/*  TOGGLE BUTTONS  */

/**
 * Circle shape Toggle Button
 * @param text Button Text
 * @param isSelected 선택 여부
 */
@Composable
fun CircleToggleButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderStroke = if(!isSelected) BorderStroke(1.dp, ToggleButtonBorderColor) else null
    val contentColor = if(!isSelected) Color.Black else Color.White
    val containerColor = if(!isSelected) Color.White else Color.Black

    Button(
        modifier = Modifier
            .size(40.dp),
        shape = CircleShape,
        border = borderStroke,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onClick,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text, style = storeMeTextStyle(FontWeight.Bold, 2), modifier = Modifier.padding(horizontal = 3.dp))
    }
}

/**
 * 기본 Toggle 버튼
 * @param text Button Text
 * @param isSelected 선택 여부
 */
@Composable
fun DefaultToggleButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderStroke = if(!isSelected) BorderStroke(1.dp, ToggleButtonBorderColor) else null
    val contentColor = if(!isSelected) Color.Black else Color.White
    val containerColor = if(!isSelected) Color.White else Color.Black

    Button(
        modifier = Modifier
            .wrapContentWidth()
            .height(26.dp)
            .defaultMinSize(
                minWidth = 20.dp,
                minHeight = ButtonDefaults.MinHeight
            ),
        shape = RoundedCornerShape(6.dp),
        border = borderStroke,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(horizontal = 10.dp),
        onClick = onClick
    ) {
        Text(text = text, style = storeMeTextStyle(FontWeight.Normal, 2), modifier = Modifier.padding(horizontal = 3.dp))
    }
}

/**
 * Default Check Button
 */
@Composable
fun DefaultCheckButton(
    text: String,
    fontWeight: FontWeight = FontWeight.Bold,
    isCheckIconOnLeft: Boolean = false,
    diffValue: Int = 0,
    selectedColor: Color = SelectedCheckBoxColor,
    enabled: Boolean = true,
    isSelected: Boolean,
    description: String = "",
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val contentColor = if(!isSelected) UndefinedTextColor else selectedColor
    val iconId = if(!isSelected) R.drawable.ic_check_off else R.drawable.ic_check_on

    Row(
        modifier = modifier
            .wrapContentSize()
            .clickable(
                enabled = enabled,
                onClick = { onClick() },
                indication = null,
                interactionSource = null
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(isCheckIconOnLeft){
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "체크 아이콘",
                modifier = Modifier
                    .size(SizeUtils.textSizeToDp(LocalDensity.current, diffValue, 4)),
                tint = contentColor
            )

            Spacer(modifier = Modifier.width(5.dp))
        }

        Text(text = text,
            style = storeMeTextStyle(fontWeight, diffValue),
            color = contentColor,
            modifier = Modifier
                .padding(vertical = 4.dp)
        )

        if(!isCheckIconOnLeft){
            Spacer(modifier = Modifier.width(5.dp))

            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "체크 아이콘",
                modifier = Modifier
                    .size(SizeUtils.textSizeToDp(LocalDensity.current, diffValue, 4)),
                tint = contentColor
            )
        }
    }

    if(description.isNotEmpty()){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = SizeUtils.textSizeToDp(
                        LocalDensity.current,
                        diffValue,
                        4
                    ) + 5.dp, top = 10.dp
                )
        ) {
            Text (
                text = description,
                style = storeMeTextStyle(FontWeight.Normal, diffValue - 2),
                color = contentColor
            )
        }
    }
}