## Security guidance for certificates in this repository

This repository contains code used by the Interstore project. It should never contain private TLS keys or other sensitive secrets.

If you need to run nginx or the app locally, follow these rules:

- Do NOT commit private keys or combined cert+key files (e.g. `cert_key.pem`) into Git.
- Keep CA/public certificates in the repo only if they are public and safe to share.
- Use the following commands to generate or concatenate files locally (do not commit them):

```bash
cd interstore/app/certs
# combine certs (developer-only)
cat *.pem > cert_key.pem
chmod 600 cert_key.pem
```

- Use `.gitignore` to prevent committing certs and keys. This repo includes a `.gitignore` entry for common cert files.

- If a private key is accidentally committed, rotate/revoke it immediately and remove it from Git history using `git filter-repo` or BFG.

- For production, use a secrets manager or the platform's key management service (KMS). Mount secrets into containers at runtime instead of baking them into images or source control.

- For CI/CD, use encrypted GitHub/GitLab secrets or HashiCorp Vault; never store private keys in pipeline code or logs.

If you find a secret in the repo, contact the repository owner and follow the incident response checklist in the README.
