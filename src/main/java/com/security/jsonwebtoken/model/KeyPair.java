package com.security.jsonwebtoken.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 공개키/개인키, 키 페어
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class KeyPair {
	/**
	 * 공개키(publicKey)
	 */
	@Schema(description = "공개키", example = "2TuPVgMCHJy5atawrsADEzjP7MCVbyyCA89UW6Wvjp9HrAwUmX56Fo9MPtUxwPxdw6MEYPF" +
			"ipqnp9pUUc1GRerR1UHTh843VB5ghkwYzYixzQ4jw4L6b1CQNWcSBRpYEHHr8PUgqkvYvJMUigQ18FvaqmWn8tMZCSwaFZK4MFSUyXPN" +
			"YQ4PFGSwjZc1VYbmTDiPN6SjYUVddDhTrmQHwbxhWLq1FX37V9zTT4enD3BfSW7zcSGdEqkNyQ1JMUc6Vd57L4d1nGs2FvFRo3UVcpVA" +
			"3nRdWwNKsDYLGryX3EwvJYntw8kMr9b3rXimGCEJihJfRQMnBYHE4smXNmZ3dUYeL1B7DSGvioZscy9nHpcdBMgb2dY2cHDZxD1XQugY" +
			"jz9ahG9yDYzTyUSd3tU")
	protected String publicKey;

	/**
	 * 개인키(privateKey)
	 */
	@SuppressWarnings("LineLength")
	@Schema(description = "개인키",
			example = "CGLrpyEbjWmqGRdPfuXXmAs3YucVQkXY8DATQqR5ogPh22efVGzRsjfJK75wEqYnEo8WRtS5xFgtQCks9vRwEJEzvgnM2J" +
					"Az5uj8ryEUEgjtc7EWLifnbeAW3RCWDuXasFisYedgkhjXJrN62ghh6VWR4qVdsTWG6tchyQFxJAVTs95EM71mohuBQ1SvNs" +
					"nsGGxLCWHtTnGitMQN3WFw9XHL5EXBgmhaG6vAivwxdqZ3qy5JJ3JpEUc37ifp4TwUHXMChDqKJRCEN7SDR89c9Dwt5Fb45K" +
					"VWztHFbsKrWnHLjjuidCnTwVZqZKo3CUq1k4ZNKgr2HMXoCXakNjVodt8ZWogN6p3eirTgittetqmJNAx1MuiJkXJdx4Ptb3" +
					"Sppvkmp9r35HJ6to58ke7AcMESL1uDH1sxikF1XJG1vPjZzPKYkaTDUaeaVwnSpXhEdWV3HgRsWyeXyX9V87mHpkp9QGhTRs" +
					"esy5YmDAAqF4VhCamiyWZUHUFiTKMtscYHeGnLy2SGzwVpgpGbTTuqDUrBLXmx2i9ZDRVkcwfpJXVgDwDJxq2qzqaqFHzYLs" +
					"krLTNDQLmojjuenFgdEfzT8twYx44Cqayf8BhxZvZ4zptv6HBEe5GWTrbCVdJYsvidQ2DsNjovc56tYxPYrjvKyKxWhj6UVh" +
					"VPxfrowTUsYZvwanCLb3Zm9wbqusf7wkyMci9b18YVY1T6rHDLahtLLhArq5a5uxV8C3Aua5YdU56c5nGBgFvAnWRbPxDJMi" +
					"xJ6nNboqhGcdr8GfVHbd9o4mBSZUqakEvccuSEDSJzWqCwt6hrYKGP9KkCT8YLWwZShHyw8UYnQBqhoQQtkT3jUViuoTymHt" +
					"eWN5a8gA4aWEj8R5oRSdGG2nvLDrBmhGFijY3gojaGppPBjb2W5ib5efiZHVoXDBveeBUR8vqzQAgj9z7tJExD9khwLomPsV" +
					"V51aejjwoRBurbL1V1MQja5DEJtU3MvyGqaTJVqCKh9tZkF28qYz187WN3CCL1BTPAEVfC1XPQwbN4nAzJjjmgpZJ67Zw8f8" +
					"Eq9FUt9qVyfrpadyXzLASVKMNp2oSiat7MiYLMbw4VmErA63qHGBrzamejFY9R77JX7iypTysPh4sgHip9rEFPeVjXki1XUq" +
					"eLgKJ9Hyoib8LdH7FnsqMq9oU93V5jEWCAPbV5zV8n7zRCZweKisdjDbkHL9nPgUfbptQQpj1hiooKxJMtsGeEgoxkJgX6H1" +
					"tLezj5SrnsrMnmRNnTsS9Y1VV4pRpKUqKiWCNBTWgBtDzFYDK7xjTfF8iBoFBShG1bFDeRfnsZtDx6xtEumK9jLwRj63bTKW" +
					"HYmGZdeXMhHrtUz1HTuByymk2D1gLzcr7DpHi6zM8LDFftkji7HMB946jpybuGAxr3AE2zhHwjzQeVE13H8X5BDbCUjRb2rC" +
					"KSR6ssn98B6qeJbPne1TEyEyNHgoM8TMDzRq3KhgDcvTw8WidusEKVJgDqAFhboGUMUHYH6zpCDRsyv4fDxv7ousBHe11pTJ" +
					"aYozAtoqNU5JWZJ1j9Wsruc8W7UgZKo8kwpsKovk7cYPsXw4P65DRyrJXF5h15tnWSa6mdozgCU8SsZ8SbMFdDaQBJTLSndZ" +
					"YtJoxLyWAyHeQvSft4PyMk8He23Jmb9Y")
	protected String privateKey;
}