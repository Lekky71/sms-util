package com.hashcode.messagedump.utils

import com.hashcode.messagedump.MainActivity
import com.itextpdf.io.font.FontConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.*
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.AreaBreak
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.VerticalAlignment
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class PdfCreator {
    companion object {


        fun createPdf(messages: JSONObject, firstPageMessage: String?, lastPageMessage:String?) {
            val outputFile = File(MainActivity().fileDir(), "pdfMessages" + "_" + System.currentTimeMillis() + ".pdf")

            val pdf = PdfDocument(PdfWriter(outputFile.absolutePath, WriterProperties().addXmpMetadata()))
            val pagesize = PageSize.A4
            val document = Document(pdf, pagesize)

            pdf.setTagged()
            pdf.catalog.lang = PdfString("en-US")
            pdf.catalog.viewerPreferences = PdfViewerPreferences().setDisplayDocTitle(true)
            val info = pdf.documentInfo
            info.title = "Messages Pdf"


            fun addNewPage(titleText: String?, bodyText: String?) {
                val bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD)

                val contentParagraph = Paragraph()
                contentParagraph.setTextAlignment(TextAlignment.JUSTIFIED)
                contentParagraph.setVerticalAlignment(VerticalAlignment.MIDDLE)
                contentParagraph.setHorizontalAlignment(HorizontalAlignment.CENTER)

                //Title Text is written in bold
                if(titleText != null) {
                    val title = Text(titleText).setFont(bold)
                    contentParagraph.add(title)
                }
                if(bodyText != null) {
                    contentParagraph.add(Text(bodyText))
                }

                document.add(contentParagraph)
                document.add(AreaBreak())

            }

            //Add first page
            if (firstPageMessage != null) {
                addNewPage(firstPageMessage, null)
            }

            //fill each page with each day's messages
            for (key in messages.keys()) {

                //Page header with date
                val date1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(key)
                val dateString = SimpleDateFormat("E, dd MMMM yyyy", Locale.getDefault()).format(date1) + "\n\n\n\n\n"

                addNewPage(dateString, messages.getString(key))

            }

            //Put a closing page
            if (lastPageMessage != null) {
                addNewPage(lastPageMessage, null)
            }

            document.close()


        }


    }

}
