---  '/jazzxdoclet/teste123' teste pasta de projeto 
--concat(root_url,'/jazzxdoclet/classes') alteracao,
select 
length (url),
url projeto,
concat(root_url,'/jazzxdoclet/classes') alteracao,
instr(concat(root_url,'/jazzxdoclet/classes'),url) AContemP
from repositories
where instr(concat(root_url,'/jazzxdoclet/classes'),url)
order by length (url) desc
limit 1


---  '/jazzxdoclet/teste123' teste alteracao na raiz do projeto com barra
select 
length (url),
url projeto,
concat(root_url,'/jazzxdoclet//') alteracao,
instr(concat(root_url,'/jazzxdoclet//'),url) AContemP
from repositories
where instr(concat(root_url,'/jazzxdoclet//'),url)
order by length (url) desc
limit 1


---  '/jazzxdoclet/teste123' teste alteracao na raiz do projeto sem barra
select 
length (url) lenn,
length (url),
url projeto,
concat(root_url,'/jazzxdoclet/') alteracao,
instr(concat(root_url,'/jazzxdoclet/'),url) AContemP
from repositories
where instr(concat(root_url,'/jazzxdoclet/'),url)
order by length (url) desc
limit 1


---  '/jazzxdoclet/teste123' teste subprojeto
select 
length (url),
url projeto,
concat(root_url,'/jazzxdoclet/subprojeto1/') alteracao,
instr(concat(root_url,'/jazzxdoclet/subprojeto1/'),url) AContemP
from repositories
where instr(concat(root_url,'/jazzxdoclet/subprojeto1/'),url)
order by length (url) desc
limit 1


---  '/jazzxdoclet/teste123' teste subprojeto com sub pasta alterada
select 
length (url),
url projeto,
concat(root_url,'/jazzxdoclet/subprojeto1/classes/') alteracao,
instr(concat(root_url,'/jazzxdoclet/subprojeto1/classes/'),url) AContemP
from repositories
where instr(concat(root_url,'/jazzxdoclet/subprojeto1/classes/'),url)
order by length (url) desc
limit 1




---  '/jazzxdoclet/teste123' teste subprojeto sem barra
select 
length (url),
url projeto,
concat(root_url,'/jazzxdoclet/subprojeto1/') alteracao,
instr(concat(root_url,'/jazzxdoclet/subprojeto1/'),url) AContemP
from repositories
where instr(concat(root_url,'/jazzxdoclet/subprojeto1/'),url)
order by length (url) desc
limit 1
