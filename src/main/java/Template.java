/**
 * Created by qinwenshi on 8/8/16.
 */
class Template
{

	private String [] body = new String [] {
			"Dear Respected One,",
			"GREETINGS,",
			"Permit me to inform you of my desire of going into business relationship with you.",
			"I got your contact from the International web site directory.",
			"I prayed over it and selected your name among other names due to it's ",
			"esteeming nature and the recommendations given to me as a reputable and trust worthy person ",
			"I can do business with and by the recommendations I must not hesitate to confide in you ",
			"for this simple and sincere business.",
			"",
			"I am Wumi Abdul; the only Daughter of late Mr and Mrs George Abdul.",
			"My father was a very wealthy cocoa merchant in Abidjan,the economic capital of Ivory Coast ",
			"before he was poisoned to death by his business associates on one of their outing to discus on a business deal.",
			"When my mother died on the 21st October 1984, my father took me and my younger brother HASSAN special ",
			"because we are motherless. Before the death of my father on 30th June 2002 in a private hospital here in Abidjan. ",
			"He secretly called me on his bedside and told me that he has a sum of $12.500.000 (Twelve Million, five hundred ",
			"thousand dollars) left in a suspense account in a local Bank here in Abidjan, that he used my name as his first ",
			"Daughter for the next of kin in deposit of the fund.",
			"He also explained to me that it was because of this wealth and some huge amount ",
			"of money his business associates supposed to balance his from the deal they had that ",
			"he was poisoned by his business associates, that I should seek for a God fearing ",
			"foreign partner in a country of my choice where I will transfer this money and use it ",
			"for investment purpose, (such as real estate management). Sir, we are honourably seeking your assistance in the following ways",
			"1) To provide a Bank account where this money would be transferred to.",
			"2) To serve as the guardian of this since I am a girl of 26 years,",
			"Moreover Sir, we are willing to offer you 15% of the sum as compensation for effort input ",
			"after the successful transfer of this fund to your designate account overseas. ",
			"please feel free to contact ,me via this email address",
			"wumi1000abdul@yahoo.com",
			"",
			"Anticipating to hear from you soon.",
			"Thanks and God Bless.",
			"Best regards.",
			"Miss Wumi Abdul",
			"",
			"PLEASE FOR PRIVATE AND SECURITY REASONS,REPLY ME VIA EMAIL:",
			"wumi1000abdul@yahoo.com",
	};

	public String make() {
		String result = "";
		for(int n = 0; n < body.length; n++) {
			result += body[n] + "\n";
		}
		return result;
	}
}
